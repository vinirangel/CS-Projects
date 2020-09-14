import java.io.*;
import java.lang.reflect.Constructor;

class CRUD <T extends Registro> {
	private String file;
    private Constructor<T> constructor;
    
    public CRUD(Constructor<T> constructor, String filename ) throws Exception {
        this.file = filename;
        this.constructor = constructor;
        RandomAccessFile arq;

        try {
            arq = new RandomAccessFile(this.file, "rw");
        }
        catch (FileNotFoundException novoArq) {
            arq = new RandomAccessFile(this.file, "rw");
        }
            arq.writeInt(1);
            arq.seek(0); //voltar ponteiro para o começo do arquivo
	}
    
    public int create(T objeto) throws Exception {
        RandomAccessFile arq = new RandomAccessFile(this.file, "rw");
        arq.seek(0);
        int proxID = arq.readInt();
        //atualizar o id do objeto
        objeto.setID(proxID);
        
        //atualizar o cabecalho
        arq.seek(0);
        arq.writeInt(proxID + 1 );
        
        byte[] ba = objeto.toByteArray();
        short tam = (short)ba.length;
        //escrever no fim do arquivo
        arq.seek(arq.length());
        //escrever o byte de lapide
        arq.writeByte(' ');
        //escrever o tamanho do registro
        arq.writeShort(tam);
        arq.write(ba);
        arq.close();

        return (proxID);
    }

	public T read(int id) throws Exception{

        RandomAccessFile arq = new RandomAccessFile(this.file, "r");
        T objeto = this.constructor.newInstance();
        arq.seek(4);
         
            while(arq.getFilePointer() < arq.length()){
                byte lapide = arq.readByte();
                short tam = arq.readShort();
                byte[] ba = new byte[tam];
                arq.read(ba);
                objeto.fromByteArray(ba);
                if(lapide == ' ' && id == objeto.getID())
                {
                    arq.close();
                    return objeto;
                }
            }
        arq.close();
        return null;
    }

    public boolean update(T novoObjeto) throws Exception {

        RandomAccessFile arq = new RandomAccessFile(this.file, "rw");
        T objeto = constructor.newInstance();
        arq.seek(4);
        int id = objeto.getID();

        while(arq.getFilePointer() < arq.length()) {
            long pos = arq.getFilePointer();
            byte lapide = arq.readByte();
            short tam = arq.readShort();
            byte[] ba = new byte[tam];
            arq.read(ba);
            objeto.fromByteArray(ba);

            if(lapide == ' ' && objeto.getID() == novoObjeto.getID() )
            {
                byte[] novoba = novoObjeto.toByteArray();
                short novotam = (short)novoba.length;
                if(novotam <= tam)
                {
                    arq.seek(pos+3);
                    arq.write(novoba);
                } else {
                    arq.seek(pos);
                    arq.write('*');
                    arq.seek(arq.length());
                    arq.write(' ');
                    arq.writeShort(novotam);
                    arq.write(novoba);
                }
                arq.close();
                return true;
            }
        }
        arq.close();
        return false;
    }

    public boolean delete(int id) throws Exception{

        RandomAccessFile arq = new RandomAccessFile(this.file, "rw");
        T objeto = constructor.newInstance();
        arq.seek(4);
         
        while(arq.getFilePointer() < arq.length()){
            long pos = arq.getFilePointer();
            byte lapide = arq.readByte();
            short tam = arq.readShort();
            byte[] ba = new byte[tam];
            arq.read(ba);
            objeto.fromByteArray(ba);
            if(lapide == ' ' && id == objeto.getID())
            {
                arq.seek(pos);
                arq.write('*');
                arq.close();
                return true;
            }
        }
        arq.close();
        return false;
    }
}