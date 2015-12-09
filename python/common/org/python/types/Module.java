package org.python.types;

public class Module extends org.python.types.Object {

    public java.lang.Class klass;

    public int hashCode() {
        return this.klass.hashCode();
    }

    protected Module() {
        this.klass = this.getClass();
    }

    public Module(java.lang.Class klass) {
        this.klass = klass;
    }

    @org.python.Method(
        __doc__ = ""
    )
    public org.python.types.Str __repr__() {
        return new org.python.types.Str(String.format("<module '%s' from '%s'>", this.typeName(), this.getClass()));
    }

    @org.python.Method(
        __doc__ = ""
    )
    public org.python.Object __getattribute__(org.python.Object name) {
        java.lang.String attr_name;
        try {
            attr_name = ((org.python.types.Str) name).value;
        } catch (java.lang.ClassCastException e) {
            throw new org.python.exceptions.TypeError("__getattribute__(): attribute name must be string");
        }
        // System.out.println("GETATTRIBUTE MODULE " + this + " " + attr_name);
        org.python.Object value;

        // First try the normal approach attribute
        org.python.types.Type cls = org.python.types.Type.pythonType(this.klass);
        // System.out.println("instance attrs = " + this.attrs);
        // System.out.println("class attrs = " + cls.attrs);
        value = cls.attrs.get(attr_name);

        if (value == null) {
            value = org.Python.builtins.get(attr_name);
        }

        if (value == null) {
            throw new org.python.exceptions.NameError(attr_name);
        }

        return value;
    }

    @org.python.Method(
        __doc__ = ""
    )
    public void __setattr__(org.python.Object name, org.python.Object value) {
        java.lang.String attr_name;
        try {
            attr_name = ((org.python.types.Str) name).value;
        } catch (java.lang.ClassCastException e) {
            throw new org.python.exceptions.TypeError("__setattr__(): attribute name must be string");
        }

        // The base object can't have attribute set on it unless the attribute already exists.
        // System.out.println("SETATTRIBUTE MODULE " + this + " " + name + " = " + value);
        org.python.types.Type cls = org.python.types.Type.pythonType(this.klass);
        // System.out.println("instance attrs = " + this.attrs);
        // System.out.println("class attrs = " + cls.attrs);

        cls.attrs.put(attr_name, value);

        // If there is a native field of the same name, set it.
        try {
            java.lang.reflect.Field field = this.getClass().getField(attr_name);
            field.set(this, value);
        } catch (NoSuchFieldException e) {
            // System.out.println("Not a native field");
        } catch (IllegalAccessException e) {
            throw new org.python.exceptions.RuntimeError("Illegal access to native field " + name);
        }
    }
}