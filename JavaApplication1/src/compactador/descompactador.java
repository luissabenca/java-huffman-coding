/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Sabença
 */
public class descompactador 
{ 
    protected meuRandomAccessFile leitor;
    protected String extAntiga;
    protected int qtoLixo;
    protected int qtosSimbolos;
    protected int[] frequencia;
    protected no<informacao>[] nos;
    protected codigo[] codigos;
    protected String nomeArq;
    
    public descompactador(String nomeArq) throws IOException
    {
        try 
        {
            this.nomeArq = nomeArq;
            this.leitor = new meuRandomAccessFile(nomeArq, "r");
            
            int qtdExtensao = leitor.read();
	    	this.extAntiga = "";
            for(int i = 0; i < qtdExtensao; i++)
                this.extAntiga += (char)leitor.read();
            
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void descompactar()
    {
        System.out.println("Lendo arquivo");
        try 
        {
            this.qtoLixo = leitor.read();
            
            this.qtosSimbolos = leitor.readInt();

            this.frequencia = new int[256];
            for (int i = 0; i < this.qtosSimbolos; i++) 
            {
                int indice = leitor.read();
                int freq = leitor.readInt();

                this.frequencia[indice] = freq;
            }
            
            this.nos = new no[256];
            this.qtosSimbolos = 0;
            
            for(int i=0; i < 256; i++)
                if(this.frequencia[i] != 0)
                {
                    this.nos[this.qtosSimbolos] = new no(new informacao(this.frequencia[i], i));
                    this.qtosSimbolos++;
                    this.mudPos(this.nos, this.qtosSimbolos);
                }
            
            System.out.println("Montando arvore");
            
            for (int qtd = this.qtosSimbolos; qtd > 1; qtd--) 
            {
                this.mudPos(this.nos, qtd);
                int soma = nos[qtd - 2].getInfo().getFrequencia() + nos[qtd - 1].getInfo().getFrequencia();

                no<informacao> aux = null;
                try 
                {
                    aux = new no<informacao>(new informacao(soma));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                aux.setEsq(this.nos[qtd - 2]);
                aux.setDir(this.nos[qtd - 1]);
                this.nos[qtd - 2] = aux;
                this.nos[qtd - 1] = null;
            }
            
            this.codigos = new codigo[256];
            
            if(this.nos[0].getEsq()==null && this.nos[0].getDir()==null && this.nos[0].getInfo().getBytee() >= 0)
				this.codigos[this.nos[0].getInfo().getBytee()] = new codigo("0");
            else
            {
                this.adicionarCodigos(this.nos[0], new codigo());
            }
            this.escreverArquivo();
            
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }       
    }
    
    protected void escreverArquivo()
    {
        System.out.println("Escrevendo arquivo");
        try
        {
            String local = this.nomeArq;
            local = local.substring(0, local.lastIndexOf('.'));
            local+= "1.";
            local += this.extAntiga;
            
            meuRandomAccessFile escritor = new meuRandomAccessFile(local, "rw");
            
            if (this.nos[0].getEsq() == null && this.nos[0].getDir() == null) 
            {
                for (long bytessss = leitor.getFilePointer(); bytessss < leitor.length(); bytessss++) {
                    int qtdBits = 8;
                    if (bytessss == leitor.length() - 1) 
                    {
                        qtdBits -= this.qtoLixo;
                    }

                    for (int i = 0; i < qtdBits; i++) 
                    {
                        escritor.write(this.nos[0].getInfo().getBytee());
                    }
                }
            } 
            else 
            {
                //escrever codigos antigos do arquivo
                no<informacao> atual = this.nos[0];
                for (long bytessss = leitor.getFilePointer(); bytessss < leitor.length(); bytessss++) 
                {
                    int byteAtual = leitor.read();

                    int qtdBits = 8;
                    if (bytessss == leitor.length() - 1) 
                    {
                        qtdBits -= this.qtoLixo;
                    }

                    for (int i = 0; i < qtdBits; i++) {
                        int bit = getBitFromByte(byteAtual, 7 - i);

                        //se acabou a arvore
                        if ((bit == 1 && atual.getDir() == null) || (bit == 0 && atual.getEsq() == null)) {
                            escritor.write(atual.getInfo().getBytee());
                            atual = this.nos[0];
                        }

                        if (bit == 0) {
                            atual = atual.getEsq();
                        } else {
                            atual = atual.getDir();
                        }

                        if (bytessss == leitor.length() && i == qtdBits - 1) //se acabou a arvore
                        {
                            if ((bit == 1 && atual.getDir() == null) || (bit == 0 && atual.getEsq() == null)) {
                                escritor.write(atual.getInfo().getBytee());
                                atual = this.nos[0];
                            }
                        }
                    }
                }

                escritor.write(atual.getInfo().getBytee());
            }
            
            escritor.close();
            this.leitor.close();
            System.out.println("Finalizado!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    protected int getBitFromByte(int bt, int p)
    {
		return ((bt >> p) & 0x01);
    }
    
    protected void adicionarCodigos(no<informacao> raiz,codigo c)
    {
        if (raiz != null) 
        {
            int simb = raiz.getInfo().getBytee();
            if (simb >= 0) 
            {
                this.codigos[simb] = (codigo) c.clone();
            } 
            else 
            {
                c.mais(0);
                this.adicionarCodigos(raiz.getEsq(), c);
                c.tirarUltimo();
                c.mais(1);
                this.adicionarCodigos(raiz.getDir(), c);
                c.tirarUltimo();
            }
        }
    }
    
    protected void mudPos(no<informacao>[] nos, int qto)
    {
        for (int i = qto - 1; i > 0; i--) 
        {
            if (nos[i].getInfo().getFrequencia() <=  nos[i - 1].getInfo().getFrequencia()) 
            {
                break;
            } 
            else 
            {
                no<informacao> aux = nos[i];
                nos[i] = nos[i - 1];
                nos[i - 1] = aux;
            }
        }
    }
}
