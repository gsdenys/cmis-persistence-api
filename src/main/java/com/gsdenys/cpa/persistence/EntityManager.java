package com.gsdenys.cpa.persistence;

import com.gsdenys.cpa.exception.CpaAnnotationException;
import com.gsdenys.cpa.exception.CpaPersistenceException;
import com.gsdenys.cpa.exception.CpaRuntimeException;

import java.util.Map;


public interface EntityManager {

    final String CPA_DEFAULT_REPOSITORY = "cpa-default-repository";

    /**
     * Make an instance managed and persistent. In case of the entity is in checkout mode the checkin
     * operation will be performed.
     * <p>
     * Sometimes this method save de obj at content management system through CMIS, sometimes it'll update it
     *
     * @param entity the object to be saved at content Management
     * @param <E>    some element
     * @throws CpaRuntimeException    some errors during persistence action
     * @throws CpaAnnotationException some error that occur when try to persist an object with annotation errors
     */
    <E> void persist(E entity) throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Make an instance managed and persistent. In case of the entity is in checkout mode the checkin
     * operation will be performed.
     * <p>
     * Sometimes this method save de obj at content management system through CMIS, sometimes it'll update it
     * <p>
     * Case the <b>lockMode</b> element was {@link Boolean#TRUE}, the element after persist action will be (or continue)
     * checked, other else the this method will performed like {@link EntityManager#persist(Object)}
     *
     * @param entity   the object to be saved at content Management
     * @param <E>      some element
     * @param lockMode the flag that define if the content node needs to be stay locked (Checkout) after operation
     * @throws CpaRuntimeException    some errors during persistence action
     * @throws CpaAnnotationException some error that occur when try to persist an object with annotation errors
     */
    <E> void persist(E entity, final Boolean lockMode) throws CpaRuntimeException, CpaAnnotationException;

    /**
     * Refresh the state of the instance from the CMIS repository, overwriting changes made to the entity, if any.
     *
     * @param entity the entity to be refreshed
     * @param <E>    some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     * @throws CpaRuntimeException    some errors during persistence action
     */
    <E> void refresh(E entity) throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Refresh the state of the instance from the CMIS repository, overwriting changes made to the entity, if any,
     * and lock it case the parameter <b>lockMode</b> was {@link Boolean#TRUE}.
     * <p>
     * In case of the <b>lockMode</b> is {@link Boolean#TRUE}, this operation is a refresh with checkout.
     *
     * @param entity   the entity to be refreshed and locked
     * @param lockMode the flag that define if the content node needs or not to be locked (Checkout)
     * @param <E>      some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     * @throws CpaRuntimeException    some errors during persistence action
     */
    <E> void refresh(E entity, final Boolean lockMode) throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Lock an entity instance that is contained in the persistence context
     *
     * @param entity   the entity to be refreshed and locked
     * @param lockMode define if the entity needs to be locked (checkout) or unlocked (checkin)
     * @param <E>      some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    <E> void lock(E entity, final Boolean lockMode) throws CpaAnnotationException;

    /**
     * Check if the entity is in lockedMode (checkout)
     *
     * @param entity the entity to be refreshed and locked
     * @param <E>    some element
     * @return boolean <b>true</b> case the entity is locked, other else <b>false</b>
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    <E> boolean isLocked(final E entity) throws CpaAnnotationException;

    /**
     * Check if the entity is in lockedMode (checkout) by <b>userName</b>
     *
     * @param entity   the entity to be refreshed and locked
     * @param userName the user name
     * @param <E>      some entity
     * @return boolean  <b>true</b> case the entity is locked by <b>userName</b>, other else <b>false</b>
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    <E> boolean isLockedBy(final E entity, final String userName) throws CpaAnnotationException;

    /**
     * Get the user name of the user that had locked the entity
     *
     * @param entity the entity to be refreshed and locked
     * @param <E>    some element
     * @return String the user name
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     */
    <E> String lockedBy(final E entity) throws CpaAnnotationException;

    /**
     * Remove the entity instance of the CMIS repository. By Default all versions will be deleted
     *
     * @param entity the entity to be removed
     * @param <E>    some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     * @throws CpaRuntimeException    some error that can be occur during the remove process
     */
    <E> void remove(E entity) throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Remove the entity instance of the CMIS repository.
     *
     * @param entity      the entity to be removed
     * @param allVersions delete all versions of entity
     * @param <E>         some element
     * @throws CpaAnnotationException error that will occur case the entity has no correctly annotated
     * @throws CpaRuntimeException    some error that can be occur during the remove process
     */
    <E> void remove(E entity, boolean allVersions) throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Move the entity from the actual folder to another one.
     *
     * @param entity     the entity to be moved
     * @param folderDest the folder to be receive the entity
     * @param <E>        some element
     * @throws CpaAnnotationException  error that will occur case the elements has no correctly annotated
     * @throws CpaPersistenceException some error that can be occur during the move process
     */
    <E> void move(E entity, final E folderDest) throws CpaAnnotationException, CpaPersistenceException;

    /**
     * Move the entity from the actual folder to another one.
     *
     * @param entity     the entity to be copied
     * @param folderDest the folder to be receive the entity
     * @param <E>        some element
     * @throws CpaAnnotationException  error that will occur case the elements has no correctly annotated
     * @throws CpaPersistenceException some error that can be occur during the copy process
     */
    <E> void copy(E entity, final E folderDest) throws CpaAnnotationException, CpaPersistenceException;

    /**
     * Obtain all properties from an entity in their actual state.
     *
     * @param entity antity to be converted to a map
     * @param <E>    some element
     * @return Map the map containing all entity properties
     * @throws CpaAnnotationException error that will occur case the elements has no correctly annotated
     * @throws CpaRuntimeException    any error at runtime
     */
    <E> Map<String, Object> getProperties(final E entity) throws CpaAnnotationException, CpaRuntimeException;


    /**
     * Obtain all properties from an entity. Case the parameter <b>refresBefore</b> is {@link Boolean#TRUE} then
     * this method will perform the {@link EntityManager#refresh(Object)} before the method execution
     *
     * @param entity       antity to be converted to a map
     * @param refresBefore boolean value to told if the entity needs to be refreshed before metadata extract
     * @param <E>          some element
     * @return Map the map containing all entity properties
     * @throws CpaAnnotationException error that will occur case the elements has no correctly annotated
     * @throws CpaRuntimeException    any error at runtime
     */
    <E> Map<String, Object> getProperties(final E entity, final Boolean refresBefore)
            throws CpaAnnotationException, CpaRuntimeException;

    /**
     * Set the properties passed by parameter to the entity. Case no properties can be mapped with any element an
     * {@link CpaRuntimeException} will be throw
     *
     * @param entity     the entity to have properties setted
     * @param properties the properties to be setted
     * @param <E>        some elements
     * @throws CpaAnnotationException error that will occur case the elements has no correctly annotated
     * @throws CpaRuntimeException    when any properties cannot be mapped at the entity
     */
    <E> void setProperties(E entity, final Map<String, Object> properties) throws CpaAnnotationException, CpaRuntimeException;
}
