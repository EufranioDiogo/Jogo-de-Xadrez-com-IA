package chessgameai.xadrez.engine.player;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.pecas.Rei;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Jogador {
    protected final Tabuleiro tabuleiro;
    protected final Rei rei;
    protected final Collection<Movimento> movimentosValidos;
    protected final Collection<Movimento> movimentosOponentes;
    private final boolean isEmCheck;
    
    
    Jogador(final Tabuleiro tabuleiro, final Collection<Movimento> movimentosValidos, 
            Collection<Movimento> movimentosOponente) {
        this.tabuleiro = tabuleiro;
        this.rei = encontrarRei();
        this.movimentosValidos = concatenarMovimentos(movimentosValidos, calcularReiCastles(movimentosValidos, movimentosOponente));
        this.movimentosOponentes = movimentosOponente;
        this.isEmCheck = !Jogador.calcularAtaquesNaCasa(this.rei.getPosicaoPeca(), movimentosOponente).isEmpty();
    }

    private Rei encontrarRei() {
        for(final Peca peca : getPecasActivas()) {
            if (peca.isRei()) {
                return (Rei) peca;
            }
        }
        throw new RuntimeException("There's no King at your chess board");
    }
    
    public boolean isMovimentoLegal(final Movimento movimento){
        return this.movimentosValidos.contains(movimento);
    }
    
    public boolean isEmCheck() {
        return this.isEmCheck;
    }
    
    public boolean isEmCheckMate() {
        return this.isEmCheck && !this.reiSemEscapatoria();
    }
    
    public boolean isEmStaleMate() {
        return !this.isEmCheck && !this.reiSemEscapatoria();
    }
    
    public boolean isCastled() {
        return false;
    }
    
    public MoveTransition fazerMovimento(final Movimento movimento) {
        if(!isMovimentoLegal(movimento)) {
            return new MoveTransition(this.tabuleiro, movimento, EstadoMovimento.MOVIMENTO_ILEGAL);
        }
        // Oponente preto
        
        final Tabuleiro tabuleiroTransicao = movimento.executarMovimento();
        
        final Collection<Movimento> ataquesRei = Jogador.calcularAtaquesNaCasa(
                tabuleiroTransicao.getJogadorActual().getOponente().getRei().getPosicaoPeca(),
                tabuleiroTransicao.getJogadorActual().getMovimentosLegais());
        
        
        if (!ataquesRei.isEmpty()) {
            return new MoveTransition(this.tabuleiro, movimento, EstadoMovimento.DEIXA_JOGADOR_EM_CHECK);
        }
        
        return new MoveTransition(tabuleiroTransicao, movimento, EstadoMovimento.DONE);
    }
    
    protected boolean reiSemEscapatoria() {
        for(final Movimento movimento : this.movimentosValidos) {
            final MoveTransition transition = fazerMovimento(movimento);
            
            if (transition.getEstadoMovimento().isDone()) {
                return true;
            }
        }
        return false;
    }
    
    public static Collection<Movimento> calcularAtaquesNaCasa(int posicaoASerAtacada, Collection<Movimento> movimentoOponentes) {
        final List<Movimento> movimentosAtaque = new ArrayList<>();
        
        for (final Movimento movimento : movimentoOponentes) {
            if (posicaoASerAtacada == movimento.getCoordenadaDestino()) {
                movimentosAtaque.add(movimento);
            }
        }
        return Collections.unmodifiableList(movimentosAtaque);
    }
    
    public Rei getRei() {
        return this.rei;
    }
    
    public Collection<Movimento> getMovimentosLegais() {
        return this.movimentosValidos;
    }
    
    public abstract Collection<Peca> getPecasActivas();
    public abstract Alliance getJogadorAlliance();
    public abstract Jogador getOponente();
    
    protected abstract Collection<Movimento> calcularReiCastles(Collection<Movimento> movimentosJogador,
            Collection<Movimento> movimentosInimigos);

    private Collection<Movimento> concatenarMovimentos(Collection<Movimento> movimentosValidos, Collection<Movimento> castleMoves) {
        Collection<Movimento> movimentos = new ArrayList<>();
        
        movimentos.addAll(movimentosValidos);
        movimentos.addAll(castleMoves);
        
        return movimentos;
    }
    
    public String mostrarMovimentos() {
        String result = "";
        
        if (this.movimentosValidos != null) {
           for (Movimento movimento :  this.movimentosValidos) {
                result += "Origem: " + movimento.getPecaMovimentada().getTipoPeca()
                        + TabuleiroUtils.getPosicaoParaCoordenada(movimento.getPecaMovimentada().getPosicaoPeca())
                        + ", Destino: "
                        + TabuleiroUtils.getPosicaoParaCoordenada(movimento.getCoordenadaDestino())
                        + "\n";
            } 
           result += "Quant Movimentos: " + this.movimentosValidos.size() + "\n";
        }
        
        
        return result;
    }
}
