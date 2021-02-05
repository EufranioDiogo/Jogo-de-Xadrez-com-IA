package chessgameai.xadrez.engine.tabuleiro;

import chessgameai.xadrez.engine.pecas.Peca;
import chessgameai.xadrez.engine.pecas.Pinhao;
import chessgameai.xadrez.engine.pecas.Torre;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro.Construtor;
import static chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils.PRIMEIRA_LINHA;
import static chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils.SEGUNDA_LINHA_PRETO;
import static chessgameai.xadrez.engine.tabuleiro.TabuleiroUtils.TERCEIRA_LINHA_PRETO;

/**
 *
 * @Autor ed
 * Free Use - Livre_Uso
 */
public abstract class Movimento {
    final Tabuleiro tabuleiro;
    final Peca pecaASerMovimentada;
    final int coordenadaDestino;
    final boolean isPrimeiroMovimento;
    public static final Movimento NULL_MOVIMENTO = new NullMove();
    
    private Movimento(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
        this.tabuleiro = tabuleiro;
        this.pecaASerMovimentada = pecaASerMovimentada;
        this.coordenadaDestino = coordenadaDestino;
        this.isPrimeiroMovimento = pecaASerMovimentada.isPrimeiroMovimento();
    }
    
    private Movimento(final Tabuleiro tabuleiro, final int coordenadaDestino) {
        this.tabuleiro = tabuleiro;
        this.pecaASerMovimentada = null;
        this.coordenadaDestino = coordenadaDestino;
        this.isPrimeiroMovimento = false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + this.coordenadaDestino;
        result = prime * result + this.pecaASerMovimentada.hashCode();
        result = prime * result + this.pecaASerMovimentada.getPosicaoPeca();
        return result;
    }
    
    @Override
    public boolean equals(final Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Movimento)) {
            return false;
        }
        final Movimento outroMovimento = (Movimento) outro;
        
        return this.getPosicaoActual() == outroMovimento.getPosicaoActual() &&
               getCoordenadaDestino() == outroMovimento.getCoordenadaDestino() &&
               getPecaMovimentada().equals(outroMovimento.getPecaMovimentada());
    }
    
    public int getCoordenadaDestino() {
        return this.coordenadaDestino;
    }
    
    public int getPosicaoActual() {
        return this.pecaASerMovimentada.getPosicaoPeca();
    }
    
    public Peca getPecaMovimentada() {
        return this.pecaASerMovimentada;
    }
    
    public boolean isAtaque() {
        return false;
    }
    
    public boolean isCastlingMove() {
        return false;
    }
    
    public Peca getPecaAtacada() {
        return null;
    }
    
    public Tabuleiro getTabuleiro() {
        return this.tabuleiro;
    }
    
    public Tabuleiro executarMovimento() {
       final Tabuleiro.Construtor constructor = new Construtor();

       for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
           if (this.pecaASerMovimentada.getPosicaoPeca() != peca.getPosicaoPeca()) {
               constructor.setPeca(peca);
           }
       }

       
       for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
           constructor.setPeca(peca);
       }
       
       constructor.setPeca(this.getPecaMovimentada().movimentarPeca(this));
       constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());
       
       Tabuleiro tab = constructor.build();
       
       return tab;
    }
    
    public static class MajorAttackMove extends MovimentoAtaque {
       
        public MajorAttackMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public boolean equals(final Object outro) {
            return this == outro || outro instanceof MovimentoSemAtaque && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return TabuleiroUtils.getPosicaoParaCoordenada(this.pecaASerMovimentada.getPosicaoPeca()) + "x"
                    + TabuleiroUtils.getPosicaoParaCoordenada(this.coordenadaDestino);
        }
       
    }
    
    public static class MovimentoAtaque extends Movimento {
        final Peca pecaASerAtacada;
        public MovimentoAtaque(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
            pecaASerAtacada = tabuleiro.getQuadrado(coordenadaDestino).getPeca();
        }
        
        @Override
        public boolean isAtaque() {
            return true;
        }
        
        @Override
        public Peca getPecaAtacada() {
            return this.pecaASerAtacada;
        }
        
        @Override
        public int hashCode() {
            return this.pecaASerAtacada.hashCode() + super.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof MovimentoAtaque)) {
                return false;
            }
            final MovimentoAtaque otherAtaque = (MovimentoAtaque) other;
            return super.equals(otherAtaque) && getPecaAtacada().equals(otherAtaque.getPecaAtacada());
        }
        
        @Override
        public String toString() {
            return TabuleiroUtils.getPosicaoParaCoordenada(this.pecaASerMovimentada.getPosicaoPeca()) + "x"
                    + TabuleiroUtils.getPosicaoParaCoordenada(this.coordenadaDestino);
        }
    }
    
    public static final class MovimentoSemAtaque extends Movimento {
        
        public MovimentoSemAtaque(final Tabuleiro tabuleiro, final Peca pecaASerMovimentada, final int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public boolean equals(final Object outro) {
            return this == outro || outro instanceof MovimentoSemAtaque && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return this.pecaASerMovimentada.getTipoPeca().toString() + TabuleiroUtils.getPosicaoParaCoordenada(coordenadaDestino);
        }
    }
    
    public static class PinhaoMovimento extends Movimento {
        
        public PinhaoMovimento(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public boolean equals(final Object outro) {
            return this == outro || outro instanceof PinhaoMovimento && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return this.pecaASerMovimentada.getTipoPeca().toString() + TabuleiroUtils.getPosicaoParaCoordenada(coordenadaDestino);
        }
    }
    
    public static class PinhaoMovimentoAtaque extends MovimentoAtaque {
        public PinhaoMovimentoAtaque(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public boolean equals(final Object outro) {
            return this == outro || outro instanceof PinhaoMovimentoAtaque && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return TabuleiroUtils.getPosicaoParaCoordenada(this.pecaASerMovimentada.getPosicaoPeca()) + "x"
                    + TabuleiroUtils.getPosicaoParaCoordenada(this.coordenadaDestino);
        }
    }
    
    public static class PinhaoEnPassantMovimentoAtaque extends PinhaoMovimentoAtaque {
        public PinhaoEnPassantMovimentoAtaque(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public boolean equals(final Object outro) {
            return this == outro || outro instanceof PinhaoMovimentoAtaque && super.equals(outro);
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            final Construtor constructor = new Construtor();
            
            for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
                if(!this.pecaASerMovimentada.equals(peca)) {
                    constructor.setPeca(peca);
                }
            }
            
            for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                if (!peca.equals(this.pecaASerAtacada)) {
                    constructor.setPeca(peca);   
                }
            }
            
            constructor.setPeca(this.pecaASerMovimentada.movimentarPeca(this));
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());
            
            return constructor.build();
        }
        
        @Override
        public String toString() {
            return TabuleiroUtils.getPosicaoParaCoordenada(this.pecaASerMovimentada.getPosicaoPeca()) + "x"
                    + TabuleiroUtils.getPosicaoParaCoordenada(this.coordenadaDestino);
        }
    }
    
    
    public static class PromocaoPinhao extends Movimento {
    
        final Movimento movimentoDecorado;
        final Pinhao pinhaoPromovido;
        
        public PromocaoPinhao(Movimento movimentoDecorado) {
            super(movimentoDecorado.getTabuleiro(), movimentoDecorado.getPecaMovimentada(), movimentoDecorado.getCoordenadaDestino());
            this.movimentoDecorado = movimentoDecorado;
            this.pinhaoPromovido = (Pinhao) movimentoDecorado.getPecaMovimentada();
        }
    
        @Override
        public Tabuleiro executarMovimento() {
            final Tabuleiro novoTabuleiro = this.movimentoDecorado.executarMovimento();
            final Tabuleiro.Construtor constructor = new Construtor();
            
            for (final Peca peca : novoTabuleiro.getJogadorActual().getPecasActivas()) {
                if (!this.pinhaoPromovido.equals(peca)) {
                    constructor.setPeca(peca);
                }
            }
            
            for (final Peca peca : novoTabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                constructor.setPeca(peca);
            }
            
            constructor.setPeca(this.pinhaoPromovido.getPromocaoPeca().movimentarPeca(this));
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());
            return constructor.build();
        }
        
        @Override
        public boolean isAtaque() {
            return this.getTabuleiro().getQuadrado(this.getCoordenadaDestino()).isQuadradoOcupado();
        }
        
        public Peca getPecaASerAtacada() {
            return null;
        }
        
        @Override
        public int hashCode() {
            return movimentoDecorado.hashCode() + (31 * this.pinhaoPromovido.hashCode());
        }
        
        @Override
        public boolean equals(Object outro) {
            return this == outro || outro instanceof PromocaoPinhao && (super.equals(outro));
        }
        
        @Override
        public String toString() {
            return this.pecaASerMovimentada.getTipoPeca().toString() + TabuleiroUtils.getPosicaoParaCoordenada(coordenadaDestino);
        }
    }
    
    public static class PinhaoSalto extends Movimento {
        public PinhaoSalto(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            final Construtor constructor = new Construtor();


            for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
                if (!this.pecaASerMovimentada.equals(peca)) {
                    constructor.setPeca(peca);
                }
            }

            for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                constructor.setPeca(peca);
            }
            final Pinhao pinhaoMovimentado = (Pinhao) pecaASerMovimentada.movimentarPeca(this);
            

            constructor.setPeca(pinhaoMovimentado);
            constructor.setEnPassantPawn(pinhaoMovimentado);
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());

            return constructor.build();
        }
        @Override
        public String toString() {
            return this.pecaASerMovimentada.getTipoPeca().toString() + TabuleiroUtils.getPosicaoParaCoordenada(coordenadaDestino);
        }
    }
    
    public abstract class CastleMove extends Movimento {
        protected final Torre castleTorre;
        protected final int castleTorrePosicaoInicio;
        protected final int castleTorrePosicaoFinal;
        
        public CastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino);
            
            this.castleTorre = castleTorre;
            this.castleTorrePosicaoInicio = castleTorrePosicaoInicio;
            this.castleTorrePosicaoFinal = castleTorrePosicaoFinal;
        }
        
        public Torre getCastleTorre() {
            return this.castleTorre;
        }
        
        
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            final Construtor constructor = new Construtor();


            for (final Peca peca : this.tabuleiro.getJogadorActual().getPecasActivas()) {
                if (!this.pecaASerMovimentada.equals(peca) && !peca.equals(this.castleTorre)) {
                    constructor.setPeca(peca);
                }
            }

            for (final Peca peca : this.tabuleiro.getJogadorActual().getOponente().getPecasActivas()) {
                constructor.setPeca(peca);
            }

            constructor.setPeca(this.getPecaMovimentada().movimentarPeca(this));
            constructor.setPeca(new Torre(this.castleTorrePosicaoFinal, this.castleTorre.getAlliancePeca()));
            constructor.setProximoJogadorAJogar(this.tabuleiro.getJogadorActual().getOponente().getJogadorAlliance());

            return constructor.build();
        }
        //exe5
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleTorre.hashCode();
            result = prime * result + this.castleTorrePosicaoFinal;
            return result;
        }
        
        @Override
        public boolean equals(final Object outro) {
            if (outro == this) {
                return true;
            }
            
            if (!(outro instanceof CastleMove)) {
                return false;
            }
            
            final CastleMove outroCastleMove = (CastleMove) outro;
            return super.equals(outroCastleMove) && this.castleTorre.equals(outroCastleMove.getCastleTorre());
        }
    }
    
    public static final class ReiSideCastleMove extends CastleMove {
        public ReiSideCastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino, castleTorre, castleTorrePosicaoInicio, castleTorrePosicaoFinal);
        }
        
        @Override
        public boolean equals(Object outro) {
            return outro == this || outro instanceof ReiSideCastleMove && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return "0-0";
        }
    }
    
    public static final class RainhaSideCastleMove extends CastleMove {
        public RainhaSideCastleMove(Tabuleiro tabuleiro, Peca pecaASerMovimentada, int coordenadaDestino,
                final Torre castleTorre, final int castleTorrePosicaoInicio, final int castleTorrePosicaoFinal) {
            super(tabuleiro, pecaASerMovimentada, coordenadaDestino, castleTorre, castleTorrePosicaoInicio, castleTorrePosicaoFinal);
        }
        
        @Override
        public boolean equals(Object outro) {
            return outro == this || outro instanceof ReiSideCastleMove && super.equals(outro);
        }
        
        @Override
        public String toString() {
            return "0-0-0";
        }
    }
    
    public static class NullMove extends Movimento {
        public NullMove() {
            super(null, -1);
        }
        
        @Override
        public Tabuleiro executarMovimento() {
            throw new RuntimeException("Não é possível executar o movimento, porque o movimento é nulo");
        }
    }
    
    public static class MovimentoFactory {
        
        private MovimentoFactory() {
            throw new RuntimeException("Not instantiable");
        }
        
        public static Movimento criarMovimento(final Tabuleiro tabuleiro, final int posicaoActual, final int posicaoDestino) {
            for(final Movimento movimento : tabuleiro.getMovimentosValidos()) {
                if (TabuleiroUtils.OITAVA_COLUNA[posicaoActual] || TabuleiroUtils.SEXTA_COLUNA[posicaoActual] || TabuleiroUtils.SETIMA_COLUNA[posicaoActual]
                    || TabuleiroUtils.OITAVA_COLUNA[posicaoDestino] || TabuleiroUtils.SEXTA_COLUNA[posicaoDestino] || TabuleiroUtils.SETIMA_COLUNA[posicaoDestino]
                    || PRIMEIRA_LINHA[posicaoActual] || SEGUNDA_LINHA_PRETO[posicaoActual] || TERCEIRA_LINHA_PRETO[posicaoActual]
                    || PRIMEIRA_LINHA[posicaoDestino] || SEGUNDA_LINHA_PRETO[posicaoDestino] || TERCEIRA_LINHA_PRETO[posicaoDestino])
                {
                    return null;
                   
                } else if (movimento.getPosicaoActual() == movimento.getCoordenadaDestino()) {
                    return null;
                }
                else if (movimento.getPosicaoActual() == posicaoActual && movimento.getCoordenadaDestino() == posicaoDestino) {
                     return movimento;
                }
 {
                    
                }
            }
            return null;
        }
    }
    
}
