package com.github.vssavin.jcrypt.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Collections;
import java.util.List;

/**
 * @author vssavin on 14.12.2023
 */
public class StubbedScriptEngineFactory implements ScriptEngineFactory {

    @Override
    public String getEngineName() {
        return "Stubbed engine";
    }

    @Override
    public String getEngineVersion() {
        return "0.1";
    }

    @Override
    public List<String> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getMimeTypes() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getNames() {
        return Collections.emptyList();
    }

    @Override
    public String getLanguageName() {
        return "";
    }

    @Override
    public String getLanguageVersion() {
        return "";
    }

    @Override
    public Object getParameter(String key) {
        return "";
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return "";
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "";
    }

    @Override
    public String getProgram(String... statements) {
        return "";
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new StubbedScriptEngine();
    }
}
