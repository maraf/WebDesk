/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public interface QueryBuilder {

    public QueryBuilder select() throws QueryBuilderException;

    public QueryBuilder select(String alias) throws QueryBuilderException;
    
    public QueryBuilder select(boolean distinct) throws QueryBuilderException;

    public QueryBuilder select(String alias, boolean distinct) throws QueryBuilderException;

    public QueryBuilder from(Class<?> clazz, String alias) throws QueryBuilderException;

    public QueryBuilder join(String property, String alias) throws QueryBuilderException;

    public QueryBuilder join(String fromAlias, String property, String alias) throws QueryBuilderException;

    public <T> QueryBuilder where(String field, QueryComparator comp, T value) throws QueryBuilderException;

    public <T> QueryBuilder where(String field, QueryComparator comp) throws QueryBuilderException;

    public <T> QueryBuilder where(String alias, String field, QueryComparator comp, T value) throws QueryBuilderException;

    public <T> QueryBuilder where(String alias, String field, QueryComparator comp) throws QueryBuilderException;

    public QueryBuilder and() throws QueryBuilderException;

    public QueryBuilder or() throws QueryBuilderException;

    public QueryBuilder order(String property, QueryOrder order) throws QueryBuilderException;

    public QueryBuilder order(String alias, String property, QueryOrder order) throws QueryBuilderException;

    public String getResult() throws QueryBuilderException;

    public QueryBuilder clear();
}
