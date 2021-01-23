package chessgameai.xadrez.engine.pecas;
import chessgameai.Alliance;
import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;
import java.util.List;
/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Peca {
    protected final TipoPeca tipoPeca;
    protected final int coluna;
    protected final int linha;
    protected final int posicaoPeca;
    protected final Alliance alliancePeca;
    protected final boolean isPrimeiroMovimento;
    private final int cachedHashCode;
    
    Peca(final int posicaoPeca, final Alliance alliancePeca, final TipoPeca tipoPeca) {
        this.posicaoPeca = posicaoPeca;
        this.alliancePeca = alliancePeca;
        this.tipoPeca = tipoPeca;
        isPrimeiroMovimento = false;
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
    
    public abstract List<Movimento> calcularPossiveisMovimentos(final Tabuleiro tabuleiro);

    public int getPosicaoPeca() {
        return posicaoPeca;
    }

    public Alliance getAlliancePeca() {
        return alliancePeca;
    }
    
    public boolean isPrimeiroMovimento() {
        return this.isPrimeiroMovimento;
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
    
    public abstract Peca movimentarPeca(Movimento move);
    
    public enum TipoPeca {
        PINHAO("P"),
        BISPO("B"),
        TORRE("T"),
        CAVALO("C"),
        REI("R"),
        RAINHA("Q");
        private String nomePeca;
        
        TipoPeca(final String nomePeca) {
            this.nomePeca = nomePeca;
        }
        
        @Override
        public String toString() {
            return this.nomePeca;
        }
    }
    
    
}
