package com.mishas.stuff.common.interfaces;


import java.io.Serializable;

public interface ICrudService<T> {

    public abstract T get(long id);

    public abstract Serializable create(T resource);

    public abstract T update(long id, T resource);

    public abstract void delete(long id);
}
