import java.io.*;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

interface Registro {
	public int getID();
	public void setID(int n);
	public byte[] toByteArray() throws IOException;
	public void fromByteArray(byte[] ba) throws IOException;
}

class CRUD<T> extends Registro {
	RandomAccessFile arq;

	public CRUD(Constructor<T> construtor, String filename ) throws Exception {
		arq = new RandomAccessFile(filename, "rw");
	}
}

class Livro implements Produto {
	private int idLivro;
	private String titulo,autor;
	private float preco;
}

public class Teste {

	// Arquivo declarado fora de main() para ser poder ser usado por outros métodos
	private static CRUD arqLivros;

	public static void main(String[] args) {

		/* Livros de exemplo
		   Livro l1 = new Livro(-1, "Eu, Robô", "Isaac Asimov", 14.9F);
		   Livro l2 = new Livro(-1, "Eu Sou A Lenda", "Richard Matheson", 21.99F);
		   Livro l3 = new Livro(-1, "Número Zero", "Umberto Eco", 34.11F);
		   int id1, id2, id3; */

		try {

			// Abre (cria) o arquivo de livros
			new File("livros.db").delete();  // apaga o arquivo anterior
			arqLivros = new Generic_CRUD<>(Livro.class.getConstructor(), "livros.db");

			// Insere os três livros
			id1 = arqLivros.create(l1); 
			l1.setID(id1);
			id2 = arqLivros.create(l2);
			l2.setID(id2);
			id3 = arqLivros.create(l3);
			l3.setID(id3);

			// Busca por dois livros
			System.out.println(arqLivros.read(id3));
			System.out.println(arqLivros.read(id1));

			// Altera um livro para um tamanho maior e exibe o resultado
			l2.autor = "Richard Burton Matheson";
			arqLivros.update(l2);
			System.out.println(arqLivros.read(id2));

			// Altera um livro para um tamanho menor e exibe o resultado
			l1.autor = "I. Asimov";
			arqLivros.update(l1);
			System.out.println(arqLivros.read(id1));

			// Excluir um livro e mostra que não existe mais
			arqLivros.delete(id3);
			Livro l = arqLivros.read(id3);
			if(l==null)
				System.out.println("Livro excluído");
			else
				System.out.println(l);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

//dos.close()
//arq.close();
