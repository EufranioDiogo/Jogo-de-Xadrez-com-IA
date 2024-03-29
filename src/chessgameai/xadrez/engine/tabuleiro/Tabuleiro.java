package chessgameai.xadrez.engine.tabuleiro;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Bispo;
import chessgameai.xadrez.engine.pecas.Cavalo;
import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.pecas.Pinhao;
import chessgameai.xadrez.engine.pecas.Rainha;
import chessgameai.xadrez.engine.pecas.Rei;
import chessgameai.xadrez.engine.pecas.Torre;
import chessgameai.xadrez.engine.player.Jogador;
import chessgameai.xadrez.engine.player.JogadorBranco;
import chessgameai.xadrez.engine.player.JogadorPreto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Tabuleiro {
    
    private final ArrayList<Quadrado> tabuleiroJogo;
    private final Collection<Peca> pecasBrancas;
    private final Collection<Peca> pecasPretas;
    
    public final JogadorBranco jogadorBranco;
    public final JogadorPreto jogadorPreto;
    public final Jogador jogadorActual;
    
    private final Pinhao enPassantPinhao;
    
    
    private Tabuleiro(Construtor construtor) {
        this.tabuleiroJogo = criarNovoTabuleiro(construtor);
        
        this.pecasBrancas = calcularPecasActivas(this.tabuleiroJogo, Alliance.WHITE);
        this.pecasPretas = calcularPecasActivas(this.tabuleiroJogo, Alliance.BLACK);
        enPassantPinhao = construtor.getEnPassantPawn();
        
        
        final Collection<Movimento> pecasBrancasMovimentosLegais = calcularMovimentosValidos(this.pecasBrancas);
        final Collection<Movimento> pecasPretasMovimentosLegais = calcularMovimentosValidos(this.pecasPretas);
        
        this.jogadorBranco = new JogadorBranco(this, pecasBrancasMovimentosLegais, pecasPretasMovimentosLegais);
        this.jogadorPreto = new JogadorPreto(this, pecasBrancasMovimentosLegais, pecasPretasMovimentosLegais);
        this.jogadorActual = construtor.proximoJogadorAJogar.proximoJogador(this.jogadorBranco, this.jogadorPreto);
    }
    
    public Collection<Movimento> calcularMovimentosValidos(final Collection<Peca> pecas) {
        ArrayList<Movimento> movimentos = new ArrayList<>();
        
        for (Peca peca : pecas) {
            final ArrayList<Movimento> possibleMoviments = peca.calcularPossiveisMovimentos(this);
            
            for (Movimento movimento : possibleMoviments) {
                if (movimento.coordenadaDestino != movimento.getPosicaoActual()) {
                    movimentos.add(movimento);
                }
            }
        }
        return movimentos;
    }
    
    
    private ArrayList<Quadrado> criarNovoTabuleiro(Construtor construtor) {
        final ArrayList<Quadrado> quadrados = new ArrayList<>();
        
        for (int i = 0; i < TabuleiroUtils.NUM_QUADRADOS; i++) {
            quadrados.add(Quadrado.criarQuadrado(i, construtor.configuracaoTabuleiro.get(i)));
        }
        return  quadrados;
    }
    
    public Tabuleiro getTabuleiro() {
        return this;
    }
    
    public Quadrado getQuadrado(final int coordenada) {
        return tabuleiroJogo.get(coordenada);
    }

    private static Collection<Peca> calcularPecasActivas(List<Quadrado> tabuleiro, Alliance alliance) {
        ArrayList<Peca> pecas = new ArrayList<>();
        
        for (final Quadrado quadrado : tabuleiro) {
            if (quadrado.isQuadradoOcupado()) {
               final Peca pecaSelecionada = quadrado.getPeca();  
                if (pecaSelecionada.getAlliancePeca() == alliance) {
                    pecas.add(pecaSelecionada);
                } 
            }
        }
        return pecas;
    }
    
    public static Tabuleiro criarJogoPadrao() {
        Construtor construtor = new Construtor();
        Alliance allianceActual = Alliance.BLACK;
        boolean setandoPecasPretas = true;
        int primeiraLinha = 3;
        int segundaLinha = 4;
        
        for (int i = 0; i < 2; i++) {
            construtor.setPeca(new Torre(primeiraLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + 0, allianceActual));

            construtor.setPeca(new Cavalo(primeiraLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + 1, allianceActual));

            construtor.setPeca(new Bispo(primeiraLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + 2, allianceActual));

            construtor.setPeca(new Rainha(primeiraLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + 3, allianceActual));
            construtor.setPeca(new Rei(primeiraLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + 4, allianceActual));
            
            for (int j = 0; j < 5; j++) {
                construtor.setPeca(new Pinhao(segundaLinha * TabuleiroUtils.NUM_QUADRADOS_POR_LINHA + j, allianceActual));
            }
            
            if (setandoPecasPretas) {
                allianceActual = Alliance.WHITE;
                setandoPecasPretas = false;
            }
            primeiraLinha = 7;
            segundaLinha = 6;
        }
        
        construtor.setProximoJogadorAJogar(allianceActual);
        
        return construtor.build();
    }
    
    public Collection<Peca> getPecasPretas() {
        return this.pecasPretas;
    }
    
    public Collection<Peca> getPecasBrancas() {
        return this.pecasBrancas;
    }
    
    public JogadorBranco getJogadorBranco() {
        return this.jogadorBranco;
    }
    
    public JogadorPreto getJogadorPreto() {
        return this.jogadorPreto;
    }
    
    public Jogador getJogadorActual() {
        return this.jogadorActual;
    }
    
    public Pinhao getEnPassantPinhao() {
        return this.enPassantPinhao;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < TabuleiroUtils.NUM_QUADRADOS; i++) {
            final String textoQuadrado = this.tabuleiroJogo.get(i).toString();
            
            if (!TabuleiroUtils.isCoordenadaValida(i)) {
                builder.append(String.format("%s", " "));
            } else {
                builder.append(String.format("%s", textoQuadrado));
            }
            
            if ((i + 1) % TabuleiroUtils.NUM_QUADRADOS_POR_LINHA == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public ArrayList<Movimento> getMovimentosValidos() {
        ArrayList<Movimento> movimentosValidos = new ArrayList<>();
        
        for (final Movimento movimento : this.getJogadorBranco().getMovimentosLegais()) {
            movimentosValidos.add(movimento);
        }
        for (final Movimento movimento : this.getJogadorPreto().getMovimentosLegais()) {
            movimentosValidos.add(movimento);
        }
        return movimentosValidos;
    }
    
     public static class Construtor {
        Map<Integer, Peca> configuracaoTabuleiro;
        Alliance proximoJogadorAJogar;
        Pinhao enPassantPawn;
        
        public Construtor() {
            configuracaoTabuleiro = new HashMap<>();
        }
        
        public Construtor setPeca(final Peca peca) {
            this.configuracaoTabuleiro.put(peca.getPosicaoPeca(), peca);
            return this;
        }
        
        public Construtor setProximoJogadorAJogar(final Alliance alliance) {
            this.proximoJogadorAJogar = alliance;
            return this;
        }
        
        public Tabuleiro build() {
            Tabuleiro tab = new Tabuleiro(this);
            return tab;
        }

        public Pinhao getEnPassantPawn() {
            return enPassantPawn;
        }

        public void setEnPassantPawn(Pinhao enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        
    }
    
}
