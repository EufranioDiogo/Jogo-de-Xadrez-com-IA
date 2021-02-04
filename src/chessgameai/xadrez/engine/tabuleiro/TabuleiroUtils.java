package chessgameai.xadrez.engine.tabuleiro;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public class TabuleiroUtils {
    public final static boolean[] PRIMEIRA_COLUNA = iniciarColunas(0);
    public final static boolean[] SEGUNDA_COLUNA = iniciarColunas(1);
    public final static boolean[] SEXTA_COLUNA = iniciarColunas(5);
    public final static boolean[] SETIMA_COLUNA = iniciarColunas(6);
    public final static boolean[] OITAVA_COLUNA = iniciarColunas(7);
    
    public static final boolean[] OITAVA_LINHA = iniciarLinha(0);
    public static final boolean[] PRIMEIRA_LINHA = iniciarLinha(0);
    public static final boolean[] SEGUNDA_LINHA_PRETO = iniciarLinha(1);
    public static final boolean[] TERCEIRA_LINHA_PRETO = iniciarLinha(2);
    public static final boolean[] SEGUNDA_LINHA = iniciarLinha(6);
    
    public static final boolean[] QUINTA_LINHA = iniciarLinha(3);
    public static final boolean[] QUARTA_LINHA = iniciarLinha(4);
    
    public static final int NUM_QUADRADOS = 64;
    public static final int NUM_QUADRADOS_POR_LINHA = 8;
    
    public static final int START_TILE_INDEX = 0;
    private static final List<String> EXPRESSAO_ALGEBRICA = inicializarExpressaoAlgebrica();
    private static final Map<String, Integer> POSICAO_PARA_COORDENADA = inicializarPosicaoParaCoordenadaMapa();
    
    
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
        return (coordenada > -1 && coordenada < NUM_QUADRADOS) && 
               !OITAVA_COLUNA[coordenada] && !SETIMA_COLUNA[coordenada] && !SEXTA_COLUNA[coordenada] &&
               !PRIMEIRA_LINHA[coordenada] && !SEGUNDA_LINHA_PRETO[coordenada] && !TERCEIRA_LINHA_PRETO[coordenada];
    }
    
    public static int getCoordenadaDaPosicao(final String posicao) {
        return POSICAO_PARA_COORDENADA.get(posicao);
    }
    
    public static String getPosicaoParaCoordenada(final int coordenada) {
        return EXPRESSAO_ALGEBRICA.get(coordenada);
    }

    private static Map<String, Integer> inicializarPosicaoParaCoordenadaMapa() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_QUADRADOS; i++) {
            positionToCoordinate.put(EXPRESSAO_ALGEBRICA.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
    
    public static List<String> inicializarExpressaoAlgebrica() {
         return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }
}
