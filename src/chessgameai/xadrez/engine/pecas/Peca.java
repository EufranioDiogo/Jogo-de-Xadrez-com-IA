package chessgameai.xadrez.engine.pecas;
import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Peca {
    protected final TipoPeca tipoPeca;
    protected final int posicaoPeca;
    protected final Alliance alliancePeca;
    protected boolean isPrimeiroMovimento;
    private final int cachedHashCode;
    
    Peca(final int posicaoPeca, final Alliance alliancePeca, final TipoPeca tipoPeca, final boolean primeiroMovimento) {
        this.posicaoPeca = posicaoPeca;
        this.alliancePeca = alliancePeca;
        this.tipoPeca = tipoPeca;
        this.isPrimeiroMovimento = primeiroMovimento;
        this.cachedHashCode = computeHashCode();
    }
    
    public boolean equals(final Peca peca) {
        if (this == peca) {
            return true;
        }
        if (!(peca instanceof Peca)) {
            return false;
        }
        final Peca outraPeca = (Peca) peca;
        
        return posicaoPeca == outraPeca.getPosicaoPeca() && tipoPeca == outraPeca.getTipoPeca() && 
               alliancePeca == outraPeca.getAlliancePeca() && isPrimeiroMovimento == outraPeca.isPrimeiroMovimento();
    }
    
    
    
    public int computeHashCode() {
        int result = tipoPeca.hashCode();
        
        result = 31 * result + alliancePeca.hashCode();
        result = 31 * result + posicaoPeca;
        result = 31 * result + (isPrimeiroMovimento ? 1 : 0);
        
        return result;
    }
    
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }
    
    public abstract ArrayList<Movimento> calcularPossiveisMovimentos(final Tabuleiro tabuleiro);

    public int getPosicaoPeca() {
        return posicaoPeca;
    }

    public Alliance getAlliancePeca() {
        return alliancePeca;
    }
    
    public boolean isPrimeiroMovimento() {
        return this.isPrimeiroMovimento;
    }
    
    public void setPrimeiroMovimento(boolean valor) {
        this.isPrimeiroMovimento = valor;
    }
    
    public TipoPeca getTipoPeca() {
        return this.tipoPeca;
    }
    
    public boolean isRei() {
        return this.tipoPeca == TipoPeca.REI;
    }
    
    public boolean isTorre() {
        return this.tipoPeca == TipoPeca.TORRE;
    }
    
    public int getValorPeca() {
        return this.tipoPeca.getValor();
    }
    
    
    public abstract Peca movimentarPeca(Movimento move);
    
    public enum TipoPeca {
        PINHAO("P") {
            @Override
            public int getValor() {
                return 100;
            }
        },
        BISPO("B") {
            @Override
            public int getValor() {
                return 300;
            }
        },
        TORRE("T") {
            @Override
            public int getValor() {
                return 500;
            }
        },
        CAVALO("C") {
            @Override
            public int getValor() {
                return 300;
            }
        },
        REI("R") {
            @Override
            public int getValor() {
                return 10000;
            }
        },
        RAINHA("Q") {
            @Override
            public int getValor() {
                return 900;
            }
        };
        private String nomePeca;
        
        TipoPeca(final String nomePeca) {
            this.nomePeca = nomePeca;
        }
        
        @Override
        public String toString() {
            return this.nomePeca;
        }
        
        public abstract int getValor();
    }
    
    
    
    
}
