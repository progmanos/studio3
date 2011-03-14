/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.json;

/**
 * SchemaProperty
 */
public class SchemaProperty
{
	private Schema _owningSchema;
	private String _name;
	private String _typeName;
	private String _description;
	private boolean _optional;
	private Object _value;

	/**
	 * SchemaProperty
	 * 
	 * @param name
	 * @param typeName
	 */
	SchemaProperty(Schema owningSchema, String name, String typeName)
	{
		this._owningSchema = owningSchema;
		this._name = name;
		this._typeName = typeName;
	}

	/**
	 * getDescription
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return this._description;
	}

	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName()
	{
		return this._name;
	}

	/**
	 * getOwningSchema
	 * 
	 * @return
	 */
	public Schema getOwningSchema()
	{
		return this._owningSchema;
	}

	/**
	 * getType
	 * 
	 * @return
	 */
	public IState getType()
	{
		return this._owningSchema.getType(this._typeName);
	}

	/**
	 * getTypeName
	 * 
	 * @return
	 */
	public String getTypeName()
	{
		return this._typeName;
	}

	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return this._value;
	}

	/**
	 * isOptional
	 * 
	 * @return the optional
	 */
	public boolean isOptional()
	{
		return this._optional;
	}

	/**
	 * setDescription
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this._description = description;
	}

	/**
	 * setOptional
	 * 
	 * @param optional
	 *            the optional to set
	 */
	public void setOptional(boolean optional)
	{
		this._optional = optional;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value)
	{
		this._value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder();

		buffer.append(this.getTypeName()).append(" ").append(this.getName()); //$NON-NLS-1$

		if (this._value != null)
		{
			buffer.append(" := ").append(this._value); //$NON-NLS-1$
		}

		return buffer.toString();
	}
}