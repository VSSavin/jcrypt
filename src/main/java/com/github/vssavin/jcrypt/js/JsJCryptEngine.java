package com.github.vssavin.jcrypt.js;

import com.github.vssavin.jcrypt.DefaultStringSafety;
import com.github.vssavin.jcrypt.JCrypt;
import com.github.vssavin.jcrypt.StringSafety;

import javax.script.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author vssavin on 03.08.2023
 */
public abstract class JsJCryptEngine implements JCrypt, JsCryptCompatible {
    protected final ScriptEngine engine;
    protected final StringSafety stringSafety;

    protected List<String> scriptsList = new ArrayList<>();

    private final List<String> phantomScriptsList = new ArrayList<>();

    protected JsJCryptEngine(StringSafety stringSafety) {
        this.stringSafety = stringSafety;
        this.engine = initEngine();
        init();
    }

    protected JsJCryptEngine() {
        this.stringSafety = new DefaultStringSafety();
        this.engine = initEngine();
    }

    private ScriptEngine initEngine() {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = engineManager.getEngineByExtension("js");

        if (scriptEngine != null) {
            return scriptEngine;
        }

        List<ScriptEngineFactory> factories = engineManager.getEngineFactories();
        if (!factories.isEmpty()) {
            return factories.get(0).getScriptEngine();
        }

        return new StubbedScriptEngine();

    }

    private void init() {
        loadScriptsFromResources();
        for (String script : getAdditionalScripts()) {
            try {
                synchronized (engine) {
                    engine.eval(script);
                }
            } catch (ScriptException e) {
                throw new JsEngineInitException("Evaluating js script error!", e);
            }
        }
    }

    protected void addPhantomScript(String script) {
        phantomScriptsList.add(script);
    }

    protected List<String> getJavaScriptsListFromResources() {
        List<String> list = new ArrayList<>();
        list.add("static/js/crypt.js");
        return list;
    }

    protected List<String> getAdditionalScripts() {
        List<String> list = new ArrayList<>();
        String btoa = "btoa = function(str) {\n" +
                "\treturn java.util.Base64.encoder.encodeToString(str.bytes);" +
                "}";

        String atob = "atob = function(str) {\n" +
                "\treturn java.util.Base64.decoder.decode(str);" +
                "}";

        list.add(btoa);
        list.add(atob);
        return list;
    }

    protected void loadScriptsFromResources() {
        scriptsList = getJavaScriptsListFromResources();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String script : scriptsList) {
            try (InputStream is = classLoader.getResourceAsStream(script);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                synchronized (engine) {
                    engine.eval(reader);
                }
            } catch (IOException | ScriptException | NullPointerException e) {
                throw new JsEngineInitException("Loading script [" + script + "] error!", e);
            }
        }
    }

    protected String getScriptForKey(String key) {
        return String.format("getKey = function() {" +
                "   return \"%s\"" +
                "}", key);
    }

    @Override
    public String encrypt(String message, String key) {
        String encrypted = "";

        try {
            if (message != null && key != null) {
                String scriptForKey = getScriptForKey(key);
                Object result;
                synchronized (engine) {
                    engine.eval(scriptForKey);
                    Invocable invocable = (Invocable) engine;
                    result = invocable.invokeFunction(getEncryptMethodName(), message);
                }

                if (result != null) {
                    String encryptedResult = result.toString();
                    encrypted = encryptedResult.replace("\0", "");
                    if (!Objects.equals(encryptedResult, encrypted)) {
                        stringSafety.clearString(encryptedResult);
                    }
                }
            }

        } catch (ScriptException | NoSuchMethodException e) {
            throw new JsEncryptionException("JS processing error!", e);
        }

        return encrypted;
    }

    @Override
    public String decrypt(String encrypted, String key) {
        String decrypted = "";

        try {
            if (key != null) {
                String scriptForKey = getScriptForKey(key);
                Object result;
                synchronized (engine) {
                    engine.eval(scriptForKey);
                    Invocable invocable = (Invocable) engine;
                    result = invocable.invokeFunction(getDecryptMethodName(), encrypted, key);
                }

                if (result != null) {
                    String decryptedResult = result.toString();
                    decrypted = decryptedResult.replace("\0", "");
                    if (!Objects.equals(decryptedResult, decrypted)) {
                        stringSafety.clearString(decryptedResult);
                    }
                }
            }

        } catch (ScriptException | NoSuchMethodException e) {
            throw new JsDecryptionException("JS processing error!", e);
        }

        return decrypted;
    }

    @Override
    public String encrypt(String message) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public String decrypt(String encrypted) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public List<String> getScriptsList() {
        List<String> result = new ArrayList<>(scriptsList);
        result.addAll(phantomScriptsList);
        return Collections.unmodifiableList(result);
    }
}
