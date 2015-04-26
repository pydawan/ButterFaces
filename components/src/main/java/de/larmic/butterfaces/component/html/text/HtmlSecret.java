package de.larmic.butterfaces.component.html.text;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head")
})
@FacesComponent(HtmlSecret.COMPONENT_TYPE)
public class HtmlSecret extends HtmlText {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.secret";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.SecretRenderer";

    public HtmlSecret() {
        super();
        this.setRendererType(RENDERER_TYPE);
        this.setType("password");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

}