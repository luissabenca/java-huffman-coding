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
public class informacao
{
    protected int frequencia;
    protected int bytee;

    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 97 * hash + this.frequencia;
        hash = 97 * hash + this.bytee;
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final informacao other = (informacao) obj;
        if (this.frequencia != other.frequencia) {
            return false;
        }
        if (this.bytee != other.bytee) {
            return false;
        }
        return true;
    }

    public void setFrequencia(int frequencia) 
    {
        this.frequencia = frequencia;
    }

    public void setBytee(int codigo) 
    {
        this.bytee = codigo;
    }

    public int getFrequencia() 
    {
        return frequencia;
    }

    public int getBytee() 
    {
        return bytee;
    }

    public informacao(int freq,int cod)
    {
        this.frequencia = freq;
        this.bytee = cod;
    }
    
    public informacao(int freq)
    {
        this.frequencia = freq;
        this.bytee = -1;
    }

    public int compareTo(informacao inf)
    {
        if (this.frequencia < inf.frequencia)
            return -1;
        if (this.frequencia == inf.frequencia)
            return 0;
        else
            return 1;
    }
    
    public String toString()
    {
        return "freq: " + this.frequencia + " bytee: " + this.bytee;
    }
}
