package chessgameai.xadrez.engine.tabuleiro;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class TabuleiroUtils {
    public final static boolean[] PRIMEIRA_COLUNA = iniciarColunas(0);
    public final static boolean[] SEGUNDA_COLUNA = iniciarColunas(1);
    public final static boolean[] SETIMA_COLUNA = iniciarColunas(6);
    public final static boolean[] OITAVA_COLUNA = iniciarColunas(7);
    
    public static final boolean[] OITAVA_LINHA = iniciarLinha(0);
    public static final boolean[] SEGUNDA_LINHA = iniciarLinha(6);
    
    
    public static final int NUM_QUADRADOS = 25;
    public static final int NUM_QUADRADOS_POR_LINHA = 5;
    
    private static final boolean[] iniciarColunas(final int coluna) {
        final boolean[] colunas = new boolean[NUM_QUADRADOS];
        int i = coluna;
        
        do {
            colunas[i] = true;
            i += NUM_QUADRADOS_POR_LINHA;
        } while (i < NUM_QUADRADOS);
        
        return colunas;
    }
    
    private static final boolean[] iniciarLinha(final int linha) {
        final boolean[] linhas = new boolean[NUM_QUADRADOS];
        int ancoraLinha = linha * 8;
        int offsetColumn = 0;
        
        do {
            linhas[ancoraLinha + offsetColumn] = true;
            offsetColumn++;
        } while (offsetColumn < NUM_QUADRADOS_POR_LINHA);
        
        return linhas;
    }
    
    public static boolean isCoordenadaValida(final int coordenada) {
        return coordenada > -1 && coordenada < NUM_QUADRADOS;
    }
}
