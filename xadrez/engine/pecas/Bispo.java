package chessgameai.xadrez.engine.pecas;

import chessgameai.Alliance;
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
public class Bispo extends Peca {
    private static final int[] possiveisOffsetsDoBispo = {-7, -9, 7, 9};
    
    public Bispo(int[] posicaoPeca, Alliance alliancePeca) {
        super(posicaoPeca, alliancePeca, TipoPeca.BISPO);
    }

    @Override
    public List<Movimento> calcularPossiveisMovimentos(Tabuleiro tabuleiro) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        
        for (final int possivelOffset : possiveisOffsetsDoBispo) {
            int coordenadaCandidataADestino = this.getPosicaoPeca();
            
            if (verificarPrimeiraColuna(this.getPosicaoPeca()) ||
                verificarOitavaColuna(this.getPosicaoPeca())) {
                int coordenadaCandidataAuxiliar = coordenadaCandidataADestino;
                int contadorLinhaActual = 7;
                Quadrado quadrado;
                coordenadaCandidataAuxiliar += possivelOffset;
                quadrado = tabuleiro.getQuadrado(coordenadaCandidataAuxiliar);
                    
                if (this.getPosicaoPeca() == 63 && possivelOffset == -9) {
                    
                    while (contadorLinhaActual > 0) {
                        if (!quadrado.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, coordenadaCandidataAuxiliar));
                        } else {
                            if (this.alliancePeca != quadrado.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                        coordenadaCandidataAuxiliar += possivelOffset;
                        quadrado = tabuleiro.getQuadrado(coordenadaCandidataAuxiliar);
                        contadorLinhaActual--;
                    }
                    
                } else if (this.getPosicaoPeca() == 56 && possivelOffset == -7) {
                    while (contadorLinhaActual > 0) {
                        if (!quadrado.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, coordenadaCandidataAuxiliar));
                        } else {
                            if (this.alliancePeca != quadrado.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                        coordenadaCandidataAuxiliar += possivelOffset;
                        quadrado = tabuleiro.getQuadrado(coordenadaCandidataAuxiliar);
                        contadorLinhaActual--;
                    }
                } else if (this.getPosicaoPeca() == 0 && possivelOffset == 9) {

                    while (contadorLinhaActual > 0) {
                        if (!quadrado.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, coordenadaCandidataAuxiliar));
                        } else {
                            if (this.alliancePeca != quadrado.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                        coordenadaCandidataAuxiliar += possivelOffset;
                        quadrado = tabuleiro.getQuadrado(coordenadaCandidataAuxiliar);
                        contadorLinhaActual--;
                    }
                } else if (this.getPosicaoPeca() == 7 && possivelOffset == 7) {
                    while (contadorLinhaActual > 0) {
                        if (!quadrado.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this, coordenadaCandidataAuxiliar));
                        } else {
                            if (this.alliancePeca != quadrado.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                        coordenadaCandidataAuxiliar += possivelOffset;
                        quadrado = tabuleiro.getQuadrado(coordenadaCandidataAuxiliar);
                        contadorLinhaActual--;
                    }
                }
            } else {
                while (TabuleiroUtils.isCoordenadaValida(coordenadaCandidataADestino)) {
                    if (verificarPrimeiraColuna(coordenadaCandidataADestino) ||
                        verificarOitavaColuna(coordenadaCandidataADestino)) {

                        Quadrado quadradoCandidato = tabuleiro.getQuadrado(coordenadaCandidataADestino);

                        if (!quadradoCandidato.isQuadradoOcupado()) {
                            movimentosPossiveis.add(new Movimento.MovimentoSemAtaque(tabuleiro, this,coordenadaCandidataADestino));
                        } else {
                            if (this.alliancePeca != quadradoCandidato.getPeca().getAlliancePeca()) {
                                movimentosPossiveis.add(new Movimento.MovimentoAtaque(tabuleiro, this, coordenadaCandidataADestino));
                            }
                            break;
                        }
                    } else {
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
        return TipoPeca.BISPO.toString();
    }

    @Override
    public Bispo movimentarPeca(Movimento move) {
        return new Bispo(move.getCoordenadaDestino(), move.getPecaMovimentada().getAlliancePeca());
    }
}
