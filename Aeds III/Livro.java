import java.io.*;

public class Livro implements Registro {
	private int id;
	public String titulo, autor;
	public float preco;

	public Livro(int idLivro, String titulo, String autor, float preco)
	{
		this.id = idLivro;
		this.titulo = titulo;
		this.autor = autor;
		this.preco = preco;
	}

	public Livro() {
		this.id = -1;
		this.titulo = "vazio";
		this.autor = "vazio";
		this.preco = 0.0F;
	}

	public int getID() {
			return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public byte[] toByteArray () throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos;
		try{
			dos = new DataOutputStream(baos);
			dos.writeInt(this.id);
			dos.writeUTF(this.titulo);
			dos.writeUTF(this.autor);
			dos.writeFloat(this.preco);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public void fromByteArray(byte[] ba) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		DataInputStream dis = new DataInputStream(bais);

		this.id = dis.readInt();
		this.titulo = dis.readUTF();
		this.autor = dis.readUTF();
		this.preco = dis.readFloat();
	}

	public String chaveSecundaria() {
		return this.titulo;
	  }
}