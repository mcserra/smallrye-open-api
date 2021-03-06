package io.smallrye.openapi.spi;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.models.Components;
import org.eclipse.microprofile.openapi.models.Constructible;
import org.eclipse.microprofile.openapi.models.ExternalDocumentation;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Paths;
import org.eclipse.microprofile.openapi.models.callbacks.Callback;
import org.eclipse.microprofile.openapi.models.examples.Example;
import org.eclipse.microprofile.openapi.models.headers.Header;
import org.eclipse.microprofile.openapi.models.info.Contact;
import org.eclipse.microprofile.openapi.models.info.Info;
import org.eclipse.microprofile.openapi.models.info.License;
import org.eclipse.microprofile.openapi.models.links.Link;
import org.eclipse.microprofile.openapi.models.media.Content;
import org.eclipse.microprofile.openapi.models.media.Discriminator;
import org.eclipse.microprofile.openapi.models.media.Encoding;
import org.eclipse.microprofile.openapi.models.media.MediaType;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.eclipse.microprofile.openapi.models.media.XML;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;
import org.eclipse.microprofile.openapi.models.parameters.RequestBody;
import org.eclipse.microprofile.openapi.models.responses.APIResponse;
import org.eclipse.microprofile.openapi.models.responses.APIResponses;
import org.eclipse.microprofile.openapi.models.security.OAuthFlow;
import org.eclipse.microprofile.openapi.models.security.OAuthFlows;
import org.eclipse.microprofile.openapi.models.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.models.security.SecurityScheme;
import org.eclipse.microprofile.openapi.models.servers.Server;
import org.eclipse.microprofile.openapi.models.servers.ServerVariable;
import org.eclipse.microprofile.openapi.models.tags.Tag;
import org.eclipse.microprofile.openapi.spi.OASFactoryResolver;

import io.smallrye.openapi.api.models.ComponentsImpl;
import io.smallrye.openapi.api.models.ExternalDocumentationImpl;
import io.smallrye.openapi.api.models.OpenAPIImpl;
import io.smallrye.openapi.api.models.OperationImpl;
import io.smallrye.openapi.api.models.PathItemImpl;
import io.smallrye.openapi.api.models.PathsImpl;
import io.smallrye.openapi.api.models.callbacks.CallbackImpl;
import io.smallrye.openapi.api.models.examples.ExampleImpl;
import io.smallrye.openapi.api.models.headers.HeaderImpl;
import io.smallrye.openapi.api.models.info.ContactImpl;
import io.smallrye.openapi.api.models.info.InfoImpl;
import io.smallrye.openapi.api.models.info.LicenseImpl;
import io.smallrye.openapi.api.models.links.LinkImpl;
import io.smallrye.openapi.api.models.media.ContentImpl;
import io.smallrye.openapi.api.models.media.DiscriminatorImpl;
import io.smallrye.openapi.api.models.media.EncodingImpl;
import io.smallrye.openapi.api.models.media.MediaTypeImpl;
import io.smallrye.openapi.api.models.media.SchemaImpl;
import io.smallrye.openapi.api.models.media.XMLImpl;
import io.smallrye.openapi.api.models.parameters.ParameterImpl;
import io.smallrye.openapi.api.models.parameters.RequestBodyImpl;
import io.smallrye.openapi.api.models.responses.APIResponseImpl;
import io.smallrye.openapi.api.models.responses.APIResponsesImpl;
import io.smallrye.openapi.api.models.security.OAuthFlowImpl;
import io.smallrye.openapi.api.models.security.OAuthFlowsImpl;
import io.smallrye.openapi.api.models.security.SecurityRequirementImpl;
import io.smallrye.openapi.api.models.security.SecuritySchemeImpl;
import io.smallrye.openapi.api.models.servers.ServerImpl;
import io.smallrye.openapi.api.models.servers.ServerVariableImpl;
import io.smallrye.openapi.api.models.tags.TagImpl;

/**
 * An implementation of the OpenAPI 1.0 spec's {@link OASFactoryResolver}. This class
 * is responsible for constructing vendor specific models given a {@link Constructible}
 * model interface.
 *
 * @author eric.wittmann@gmail.com
 */
public class OASFactoryResolverImpl extends OASFactoryResolver {

    private static final Map<Class<? extends Constructible>, Class<? extends Constructible>> registry = new HashMap<>();
    static {
        registry.put(APIResponse.class, APIResponseImpl.class);
        registry.put(APIResponses.class, APIResponsesImpl.class);
        registry.put(Callback.class, CallbackImpl.class);
        registry.put(Components.class, ComponentsImpl.class);
        registry.put(Contact.class, ContactImpl.class);
        registry.put(Content.class, ContentImpl.class);
        registry.put(Discriminator.class, DiscriminatorImpl.class);
        registry.put(Encoding.class, EncodingImpl.class);
        registry.put(Example.class, ExampleImpl.class);
        registry.put(ExternalDocumentation.class, ExternalDocumentationImpl.class);
        registry.put(Header.class, HeaderImpl.class);
        registry.put(Info.class, InfoImpl.class);
        registry.put(License.class, LicenseImpl.class);
        registry.put(Link.class, LinkImpl.class);
        registry.put(MediaType.class, MediaTypeImpl.class);
        registry.put(OAuthFlow.class, OAuthFlowImpl.class);
        registry.put(OAuthFlows.class, OAuthFlowsImpl.class);
        registry.put(OpenAPI.class, OpenAPIImpl.class);
        registry.put(Operation.class, OperationImpl.class);
        registry.put(Parameter.class, ParameterImpl.class);
        registry.put(PathItem.class, PathItemImpl.class);
        registry.put(Paths.class, PathsImpl.class);
        registry.put(RequestBody.class, RequestBodyImpl.class);
        registry.put(Schema.class, SchemaImpl.class);
        registry.put(SecurityRequirement.class, SecurityRequirementImpl.class);
        registry.put(SecurityScheme.class, SecuritySchemeImpl.class);
        registry.put(Server.class, ServerImpl.class);
        registry.put(ServerVariable.class, ServerVariableImpl.class);
        registry.put(Tag.class, TagImpl.class);
        registry.put(XML.class, XMLImpl.class);
    }

    /**
     * @see org.eclipse.microprofile.openapi.spi.OASFactoryResolver#createObject(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Constructible> T createObject(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        try {
            Class<? extends Constructible> implClass = registry.get(clazz);
            if (implClass == null) {
                throw new IllegalArgumentException("Class '" + clazz.getName() + "' is not Constructible.");
            }
            return (T) implClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
