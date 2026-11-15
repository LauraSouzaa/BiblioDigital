package br.edu.atitus.bibliodigital.repositories;
import br.edu.atitus.bibliodigital.entities.Livro;
import  java.util.ArrayList;
import java.util.List;

public class LivroRepository {

    private List<Livro> acervo = new ArrayList<>();

    public List<Livro> findAll() {
        return new ArrayList<>(acervo);
    }

    public void inserir(Livro livro) { acervo.add(livro); }

    public Livro buscarPorIndice(int indice) { return acervo.get(indice);}

    public Livro findByTituloExato(String titulo) {
        for (Livro livro : acervo) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) { return livro; }
        }
        return null;
    }
    public List<Livro> findByTitulo(String parametro) {List<Livro> resultado = new ArrayList<>();

        for (Livro livro : acervo) {
            if (livro.getTitulo().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
    return resultado;
    }
    public List<Livro> findByAutor(String parametro) {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : acervo) {
            if (livro.getAutor().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }
    public void alterar(int indice, Livro livro) {
        acervo.set(indice, livro);
    }

    public void excluir(int indice) {
        acervo.remove(indice);
}
}




