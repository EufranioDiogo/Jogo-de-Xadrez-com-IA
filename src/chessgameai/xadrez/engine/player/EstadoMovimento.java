package chessgameai.xadrez.engine.player;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public enum EstadoMovimento {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    MOVIMENTO_ILEGAL {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    DEIXA_JOGADOR_EM_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
