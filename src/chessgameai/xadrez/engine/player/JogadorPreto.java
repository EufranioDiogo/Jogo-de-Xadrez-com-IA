package chessgameai.xadrez.engine.player;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.pecas.Torre;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
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
public class JogadorPreto extends Jogador{
    
    public JogadorPreto(Tabuleiro tabuleiro, Collection<Movimento> movimentosBrancas, Collection<Movimento> movimentosPretas) {
        super(tabuleiro, movimentosPretas, movimentosBrancas);
    }

    @Override
    public Collection<Peca> getPecasActivas() {
        return this.tabuleiro.getPecasPretas();
    }

    @Override
    public Alliance getJogadorAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Jogador getOponente() {
        return this.tabuleiro.getJogadorBranco();
    }
    
    @Override
    public String toString() {
        return "JogadorPreto{" + '}';
    }

    @Override
    protected Collection<Movimento> calcularReiCastles(final Collection<Movimento> movimentosJogador, final Collection<Movimento> movimentosInimigos) {
        final List<Movimento> reiCastles = new ArrayList<>();
        
        if (this.rei.isPrimeiroMovimento() && !this.isEmCheck()) {
            if (!this.tabuleiro.getQuadrado(5).isQuadradoOcupado() &&
                !this.tabuleiro.getQuadrado(6).isQuadradoOcupado()) {
                Quadrado quadradoTorre = this.tabuleiro.getQuadrado(7);
                
                if (quadradoTorre.isQuadradoOcupado() && quadradoTorre.getPeca().isPrimeiroMovimento()) {
                    if (Jogador.calcularAtaquesNaCasa(5, this.getOponente().getMovimentosLegais()).isEmpty() &&
                        Jogador.calcularAtaquesNaCasa(6, this.getOponente().getMovimentosLegais()).isEmpty() &&
                        quadradoTorre.getPeca().isTorre()) {
                        reiCastles.add(new Movimento.ReiSideCastleMove(this.tabuleiro, this.rei, 6, (Torre) quadradoTorre.getPeca(),
                                quadradoTorre.getPeca().getPosicaoPeca(), 5));
                    }
                }
            }
            
            if(!this.tabuleiro.getQuadrado(1).isQuadradoOcupado() &&
               !this.tabuleiro.getQuadrado(2).isQuadradoOcupado() &&
               !this.tabuleiro.getQuadrado(3).isQuadradoOcupado()) {
                Quadrado quadradoTorre = this.tabuleiro.getQuadrado(TabuleiroUtils.NUM_QUADRADOS - 8);
                
                if (quadradoTorre.isQuadradoOcupado() && quadradoTorre.getPeca().isPrimeiroMovimento()) {
                    if (Jogador.calcularAtaquesNaCasa(2, this.getOponente().getMovimentosLegais()).isEmpty() &&
                        Jogador.calcularAtaquesNaCasa(3, this.getOponente().getMovimentosLegais()).isEmpty() &&
                        Jogador.calcularAtaquesNaCasa(4, this.getOponente().getMovimentosLegais()).isEmpty() &&
                            quadradoTorre.getPeca().isTorre()) {
                        reiCastles.add(new Movimento.RainhaSideCastleMove(this.tabuleiro, this.rei, 2, (Torre) quadradoTorre.getPeca(),
                                quadradoTorre.getPeca().getPosicaoPeca(), 3));
                    }
                }
            }
        }
        return Collections.unmodifiableList(reiCastles);
    }

}
