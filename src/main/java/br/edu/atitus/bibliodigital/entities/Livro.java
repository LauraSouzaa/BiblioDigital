package br.edu.atitus.bibliodigital.entities;

public abstract class Livro {

    public static final int ANO_PUBLICACAO_MINIMO = 1900;
    private String titulo;
    private String autor;
    private int anoPublicacao;

    // Método construtor
    public Livro(String titulo, String autor, int anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }

    public abstract String getTipo();

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    @Override 
    public String toString() {
        return "Título: %s\n - Autor: %s\n - Ano de Publicação: %d\n - Tipo: %s".formatted(titulo, autor, anoPublicacao, getTipo());
    }
}