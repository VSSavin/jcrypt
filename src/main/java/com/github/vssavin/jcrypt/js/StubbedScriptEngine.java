package com.github.vssavin.jcrypt.js;

import javax.script.*;
import java.io.Reader;

/**
 * @author vssavin on 14.12.2023
 */
public class StubbedScriptEngine extends AbstractScriptEngine implements Compilable, Invocable  {

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

    @Override
    public CompiledScript compile(String script) {
        return new StubbedCompiledScript();
    }

    @Override
    public CompiledScript compile(Reader script) {
        return new StubbedCompiledScript();
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args) {
        return createStubbedObject(args);
    }

    @Override
    public Object invokeFunction(String name, Object... args) {
        return createStubbedObject(args);
    }

    @Override
    public <T> T getInterface(Class<T> clasz) {
        return null;
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clasz) {
        return null;
    }

    private Object createStubbedObject(Object... args) {
        if (args == null || args.length == 0) {
            return "Stubbed object";
        } else {
            return args[0];
        }
    }

    private class StubbedCompiledScript extends CompiledScript {

        @Override
        public Object eval(ScriptContext context) {
            return new Object();
        }

        @Override
        public ScriptEngine getEngine() {
            return StubbedScriptEngine.this;
        }
    }
}
