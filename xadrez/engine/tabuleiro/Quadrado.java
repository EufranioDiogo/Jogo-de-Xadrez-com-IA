package chessgameai.xadrez.engine.tabuleiro;
import chessgameai.Alliance;
import chessgameai.xadrez.engine.pecas.Peca;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Quadrado {
    protected final int cordenadaQuadrado;
    private static final Map<Integer, QuadradoVazio> QUADRADOS_VAZIOS_CACHE = criarTodosOsQuadradosVazios();
    
    
    private Quadrado(int cordenadaQuadrado) {
        this.cordenadaQuadrado = cordenadaQuadrado;
    }
    
    public static Quadrado criarQuadrado(final int coordenada, final Peca peca) {
        return peca != null ? new QuadradoOcupado(coordenada, peca) : QUADRADOS_VAZIOS_CACHE.get(coordenada);
    }
    
    private static Map<Integer, QuadradoVazio> criarTodosOsQuadradosVazios() {
        Map<Integer, QuadradoVazio> quadradosVazios = new HashMap<>();
        
        for (int i = 0; i < TabuleiroUtils.NUM_QUADRADOS; i++) {
            quadradosVazios.put(i, new QuadradoVazio(i));
        }
        return Collections.unmodifiableMap(quadradosVazios);
    }
    
    public abstract boolean isQuadradoOcupado();

    public abstract Peca getPeca();
    
    
    public static final class QuadradoVazio extends Quadrado {
        private QuadradoVazio(final int cordenada) {
            super(cordenada);
        }
        
        @Override
        public boolean isQuadradoOcupado() {
            return false;
        }

        @Override
        public Peca getPeca() {
            return null;
        }
        @Override
        public String toString() {
            return "-";
        }
    }
    
    public static final class QuadradoOcupado extends Quadrado {
        private final Peca peca;
        
        private QuadradoOcupado(int cordenada, Peca peca) {
            super(cordenada);
            this.peca = peca;
        }
        
        @Override
        public boolean isQuadradoOcupado() {
            return true;
        }

        @Override
        public Peca getPeca() {
            return peca;
        }
        @Override
        public String toString() {
            return getPeca().getAlliancePeca() == Alliance.BLACK ?  getPeca().toString().toLowerCase() : 
                    getPeca().toString().toUpperCase();
        }
    }
}
