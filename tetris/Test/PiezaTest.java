import model.Pieza;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class PiezaTest {

    @Test
    public void testCreacionPiezaAleatoria() {
        // Probamos que al pedir una pieza random, no llegue nula
        Pieza p = Pieza.randomPieza(new Random());
        assertNotNull("La pieza no debería ser nula", p);
        assertNotNull("La forma de la pieza no debería ser nula", p.getForma());
    }

    @Test
    public void testRotacionPieza() {
        
        Pieza p = Pieza.randomPieza(new Random());
        int[][] formaOriginal = p.getForma();
        int[][] formaRotada = p.getRotada();
        
        // Verificar que la matriz rotada tiene el mismo tamaño
        assertEquals(4, formaRotada.length);
        assertEquals(4, formaRotada[0].length);
        
        // Verificar que NO es la misma referencia de objeto (es una matriz nueva)
        assertNotSame(formaOriginal, formaRotada);
    }
}