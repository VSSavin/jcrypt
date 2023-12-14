package com.github.vssavin.jcrypt.js;

import javax.script.*;
import java.io.Reader;

/**
 * @author vssavin on 14.12.2023
 */
public class StubbedScriptEngine extends AbstractScriptEngine {

    private static final String MESSAGE = "No evaluations available. This is just a stubbed engine!";

    @Override
    public Object eval(String script, ScriptContext context) {
        return MESSAGE;
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) {
        return MESSAGE;
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return new StubbedScriptEngineFactory();
    }
}
