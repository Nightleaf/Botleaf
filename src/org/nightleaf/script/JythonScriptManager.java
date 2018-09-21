package org.nightleaf.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonScriptManager {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(JythonScriptManager.class
	    .getName());

    /**
     * The python interpreter object.
     */
    private PythonInterpreter interpreter;

    /**
     * Constructor
     */
    public JythonScriptManager() {
	interpreter = new PythonInterpreter();
    }

    /**
     * Loads the scripts into memory.
     */
    public void load() {
	interpreter.cleanup();
	File scriptDir = new File("./scripts/");
	int parsed = 0;
	if (scriptDir.isDirectory()) {
	    File[] children = scriptDir.listFiles();
	    for (File child : children) {
		if (child.isFile() && child.getName().endsWith(".py")) {
		    try {
			interpreter.execfile(new FileInputStream(child));
		    } catch (IOException ioe) {
			ioe.printStackTrace();
		    }
		    parsed++;
		}
	    }
	}
	System.out.println("Loaded " + parsed + " scripts..");
    }

    /**
     * Invokes a python script.
     * 
     * @param name
     *            The method name.
     * @param params
     *            The parameters
     */
    public boolean invoke(String name, Object... params) {
	try {
	    PyObject obj = interpreter.get(name);
	    if (obj != null && obj instanceof PyFunction) {
		PyFunction func = (PyFunction) obj;
		PyObject[] objects = new PyObject[params.length];
		for (int i = 0; i < params.length; i++) {
		    Object bind = params[i];
		    objects[i] = Py.java2py(bind);
		}
		func.__call__(objects);
	    } else {
		return false;
	    }
	    return true;
	} catch (PyException e) {
	    System.out.println(e.type);
	    System.out.println(e.value);
	    System.out.println(e.traceback);
	    logger.severe("Problem while parsing: " + name + ":");
	    return false;
	}
    }

}
