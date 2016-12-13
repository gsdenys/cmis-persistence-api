package org.cmis.cpa.annotations;

/**
 * Annotation definition for CMIS connection class. This annotation defines the elements to connect with a CMIS
 * repository.
 *
 * The connection can be passed by connector class by a annotation (url, user and password) or by
 * propertyFile (propertyFile). Case none of this fields was defined and the project have no
 * <code>cmis.properties</code> file at classpath, the parser will return a
 * {@link org.cmis.cpa.exception.CpaAnnotationException} error.
 *
 * The structure of <code>cmis.properties</code> can be better explained at the project documentation. Follow you can found a little
 * bit about this structure.
 *
 * <table>
 *     <thead>
 *         <tr>
 *             <td>Parameter</td>
 *             <td>Description</td>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>org.cmis.cpa.url</td>
 *             <td>Define the CMIS connection URL</td>
 *         </tr>
 *         <tr>
 *             <td>org.cmis.cpa.user</td>
 *             <td>Define the CMIS user</td>
 *         </tr>
 *         <tr>
 *             <td>org.cmis.cpa.password</td>
 *             <td>Define the CMIS user password</td>
 *         </tr>
 *         <tr>
 *             <td>org.cmis.cpa.repositoryId</td>
 *             <td>Define the CMIS repository id</td>
 *         </tr>
 *     </tbody>
 * </table>
 *
 * To know how to use multiple CMIS repository connection go to project documentation page.
 *
 * @author Denys G. Santos (gsdenys@gmail.com)
 * @version 0.0.1
 * @since 0.0.1
 */
public @interface CPAConnection {

    /**
     * The CMIS url. Use in this field the CMIS 1.1 URL.
     *
     * @return String
     *          the url connection
     */
    String url() default "";

    /**
     * The CMIS user.
     *
     * @return String
     *          The CMIS user
     */
    String user() default "";

    /**
     * The CMIS password
     *
     * @return String
     *          The CMIS password
     */
    String password() default "";

    /**
     * The Repository ID. the Default is the first repo from CMIS repository search.
     *
     * @return String
     *          The repository ID
     */
    String repositoryId() default "";

    /**
     * The CMIS connection property file name. By default it is <code>cmis.properties</code>
     *
     * @return String
     *           The name of CMIS property file
     */
    String propertyFile() default "";
}
