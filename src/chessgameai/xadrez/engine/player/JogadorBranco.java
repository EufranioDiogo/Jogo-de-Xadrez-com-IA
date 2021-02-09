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
public class JogadorBranco extends Jogador {
    
    public JogadorBranco(Tabuleiro tabuleiro, Collection<Movimento> movimentosBrancas, Collection<Movimento> movimentosPretas) {
        super(tabuleiro, movimentosBrancas, movimentosPretas);
    }

    @Override
    public Collection<Peca> getPecasActivas() {
        return this.tabuleiro.getPecasBrancas();
    }

    @Override
    public Alliance getJogadorAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public JogadorPreto getOponente() {
        return this.tabuleiro.getJogadorPreto();
    }

    @Override
    public String toString() {
        return "JogadorBranco ";
    }
    
    

    @Override
    protected Collection<Movimento> calcularReiCastles(final Collection<Movimento> movimentosJogador, final Collection<Movimento> movimentosInimigos) {
        final List<Movimento> reiCastles = new ArrayList<>();
        
        /*if (this.rei.isPrimeiroMovimento() && !this.isEmCheck()) {
            
            if (this.tabuleiro.getQuadrado(61).getPeca() == null && this.tabuleiro.getQuadrado(62).getPeca() == null) {
                if (!this.tabuleiro.getQuadrado(61).isQuadradoOcupado() &&
                !this.tabuleiro.getQuadrado(62).isQuadradoOcupado()) {
                Quadrado quadradoTorre = this.tabuleiro.getQuadrado(63);
                
                    if (quadradoTorre.isQuadradoOcupado() && quadradoTorre.getPeca().isPrimeiroMovimento()) {
                        System.out.println(this.tabuleiro.getJogadorPreto());
                        if (Jogador.calcularAtaquesNaCasa(61, this.tabuleiro.getJogadorPreto().getMovimentosLegais()).isEmpty() &&
                            Jogador.calcularAtaquesNaCasa(62, this.tabuleiro.getJogadorPreto().getMovimentosLegais()).isEmpty() &&
                            quadradoTorre.getPeca().isTorre()) {
                            reiCastles.add(new Movimento.ReiSideCastleMove(this.tabuleiro, this.rei, 62, (Torre) quadradoTorre.getPeca(),
                                    quadradoTorre.getPeca().getPosicaoPeca(), 61));
                        }
                    }
                }
            } else if (this.tabuleiro.getQuadrado(59).getPeca() == null && this.tabuleiro.getQuadrado(58).getPeca() == null &&
                    this.tabuleiro.getQuadrado(57).getPeca() == null) {
                if(!this.tabuleiro.getQuadrado(59).isQuadradoOcupado() &&
                    !this.tabuleiro.getQuadrado(58).isQuadradoOcupado() &&
                    !this.tabuleiro.getQuadrado(57).isQuadradoOcupado()) {
                      Quadrado quadradoTorre = this.tabuleiro.getQuadrado(56);

                      if (quadradoTorre.isQuadradoOcupado() && quadradoTorre.getPeca().isPrimeiroMovimento()) {
                          if (Jogador.calcularAtaquesNaCasa(58, this.getOponente().getMovimentosLegais()).isEmpty() &&
                              Jogador.calcularAtaquesNaCasa(59, this.getOponente().getMovimentosLegais()).isEmpty() &&
                                  quadradoTorre.getPeca().isTorre()) {
                              reiCastles.add(new Movimento.RainhaSideCastleMove(this.tabuleiro, this.rei, 58, (Torre) quadradoTorre.getPeca(),
                                      quadradoTorre.getPeca().getPosicaoPeca(), 59));
                          }
                      }
                  }
            }
        }*/
        return Collections.unmodifiableList(reiCastles);
    }
    
}
