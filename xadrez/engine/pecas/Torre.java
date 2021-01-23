package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Quadrado;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class Torre extends Peca {

    private static final int[] possiveisOffsetsDaTorre = {-8, 8, -1, 1};
    
    public Torre(int posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.TORRE);
    }

    @Override
    public List<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        for (final int possivelOffset : possiveisOffsetsDaTorre) {
            int coordenadaCandidataADestino = this.getPosicaoPeca();
            
            if (possivelOffset == 1 || possivelOffset == -1) {
                int limiteSuperior = 0;
                int limiteInferior = 0;
                
                if (coordenadaCandidataADestino > -1 && coordenadaCandidataADestino < 8) {
                    limiteInferior = 0;
                    limiteSuperior = 7;
                } else if (coordenadaCandidataADestino > 7 && coordenadaCandidataADestino < 16) {
                    limiteInferior = 8;
                    limiteSuperior = 15;
                } else if (coordenadaCandidataADestino > 15 && coordenadaCandidataADestino < 24) {
                    limiteInferior = 16;
                    limiteSuperior = 23;
                } else if (coordenadaCandidataADestino > 23 && coordenadaCandidataADestino < 32) {
                    limiteInferior = 24;
                    limiteSuperior = 31;
                } else if (coordenadaCandidataADestino > 31 && coordenadaCandidataADestino < 40) {
                    limiteInferior = 32;
                    limiteSuperior = 39;
                } else if (coordenadaCandidataADestino > 39 && coordenadaCandidataADestino < 48) {
                    limiteInferior = 40;
                    limiteSuperior = 47;
                } else if (coordenadaCandidataADestino > 47 && coordenadaCandidataADestino < 56) {
                    limiteInferior = 48;
                    limiteSuperior = 55;
                } else if (coordenadaCandidataADestino > 55 && coordenadaCandidataADestino < 64) {
                    limiteInferior = 56;
                    limiteSuperior = 63;
                }
                
                while (coordenadaCandidataADestino > limiteInferior && coordenadaCandidataADestino < limiteSuperior) {
                    coordenadaCandidataADestino += possivelOffset;
                    Quadrado quadrado = tabuleiro.getQuadrado(coordenadaCandidataADestino);

                    if (!quadrado.isQuadradoOcupado()) {
                        movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this,coordenadaCandidataADestino));
                    } else {
                        if (this.alliancePeca != quadrado.getPeca().getAlliancePeca()) {
                            movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));   
                        }
                        break;
                    }
                }
            } else {
                while (TabuleiroUtils.isCoordenadaValida(coordenadaCandidataADestino)) {
                    coordenadaCandidataADestino += possivelOffset;

                    if(TabuleiroUtils.isCoordenadaValida(coordenadaCandidataADestino)) {
                        Quadrado quadradoCandidato = tabuleiro.getQuadrado(coordenadaCandidataADestino);

                        if (!quadradoCandidato.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this,coordenadaCandidataADestino));
                        } else {
                            if (this.alliancePeca != quadradoCandidato.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(movimentosPossiveis);
    }

    private boolean verificarOitavaColuna(int coordenada) {
        return TabuleiroUtils.OITAVA_COLUNA[coordenada];
    }
    
    private boolean verificarPrimeiraColuna(int coordenada) {
        return TabuleiroUtils.PRIMEIRA_COLUNA[coordenada];
    }
    @Override
    public String toString() {
        return TipoPeca.TORRE.toString();
    }
    @Override
    public Torre movimentarPeca(Movimento move) {
        return new Torre(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
}
