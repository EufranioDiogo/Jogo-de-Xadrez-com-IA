package chessgameai.xadrez.engine.player;

import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class MoveTransition {
    private final Tabuleiro tabuleiroTransicao;
    private final Movimento movimento;
    private final EstadoMovimento estadoMovimento;
    
    public MoveTransition(final Tabuleiro tabuleiro, final Movimento movimento, final EstadoMovimento estado) {
        this.tabuleiroTransicao = tabuleiro;
        this.movimento = movimento;
        this.estadoMovimento = estado;
    }
    
    public EstadoMovimento getEstadoMovimento() {
        return this.estadoMovimento;
    }
    
    public Tabuleiro getTabuleiro() {
        return this.tabuleiroTransicao;
    }
}
