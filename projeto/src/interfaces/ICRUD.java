package interfaces;

import java.util.List;

public interface ICRUD<T> {

    T salvar(T obj);

    void deletar(int id);

    void alterar(T obj);

    T consultar(int id);

    List<T> consultar();
}