package com.mishas.stuff.common.service;

import java.io.Serializable;

public interface ICrudService<T> {

    public abstract T get(long id);

    public abstract void create(T resource);

    public abstract T update(long id, T resource);

    public abstract void delete(long id);
}
