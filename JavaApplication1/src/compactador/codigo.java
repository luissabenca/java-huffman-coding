/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactador;

/**
 *
 * @author Luis Sabença
 */
public class codigo
{
	protected String cod = "";
	
	public codigo()
	{}
	
	public codigo(String c)
	{
		this.cod = c;
	}

	public void mais(int i)
	{
		this.cod += i;
	}
	
	public void mais(byte[] b)
	{
		for (int i = 0; i < b.length; i++)
			this.cod += b[i];
	}

	public byte getByte()
	{
		return (byte)Integer.parseInt(this.cod);
	}
        
	public int getQtdBits()
	{
		return this.cod.length();
	}

	public void tirarUltimo()
	{
		this.cod = this.cod.substring(0, this.cod.length() - 1);
	}

	public codigo(codigo c)
	{
		this.cod = c.cod;
	}

	public Object clone()
	{
		codigo ret = null;

		try
		{
			ret = new codigo(this);
		}catch(Exception e)
		{}

		return ret;
	}
	
	public String getCodigo()
	{
		return this.cod;
	}
	
	public String toString()
	{
		return this.cod;
	}

	public int getInt() 
	{
		if(this.cod != "")
			return Integer.parseInt(this.cod);
		
		return 0;
	}
}