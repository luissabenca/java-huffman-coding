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
public class no <X>
{
        protected no esq;
        protected X  info;
        protected no dir;

        public no getEsq ()
        {
            return this.esq;
        }

        public X getInfo ()
        {
            return this.info;
        }

        public no getDir ()
        {
            return this.dir;
        }

        public void setEsq (no e)
        {
            this.esq=e;
        }

        public void setInfo (X x)
        {
            this.info=x;
        }

        public void setDir (no d)
        {
            this.dir=d;
        }

        public no (no e, X x, no d)
        {
            this.esq =e;
            this.info=x;
            this.dir =d;
        }

        public no (X x)
        {
            this (null,x,null);
        }
        
             

    @Override
    public String toString() 
    {
        //if(this.esq == null && this.dir == null)
            return this.info.toString();
        
        //return this.info.toString() + this.esq.toString() + this.dir.toString();
    }
    
    public boolean ehFolha()
    {
        if(this.esq == null && this.dir == null)
            return true;
        
        return false;
    }        
}
