package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.model.table.SortType;
import de.larmic.butterfaces.resolver.AjaxRequest;
import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlColumn.COMPONENT_FAMILY, rendererType = HtmlColumn.RENDERER_TYPE)
public class ColumnRenderer extends com.sun.faces.renderkit.html_basic.HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlColumn column = (HtmlColumn) component;
        final HtmlTable table = this.findParentTable(component);
        final int columnNumber = column.getColumnNumberUsedByTable();
        final AjaxRequest ajaxRequest = column.getTableAjaxClickRequest();
        final WebXmlParameters webXmlParameters = column.getWebXmlParameters();
        final HtmlTooltip tooltip = this.findTooltip(component);

        writer.startElement("th", component);
        writer.writeAttribute("id", column.getClientId(), null);
        if (column.isSortColumnEnabled() && table.getTableSortModel() != null) {
            writer.writeAttribute("class", "butter-component-table-column-header butter-component-table-column-sort", null);
        } else {
            writer.writeAttribute("class", "butter-component-table-column-header", null);
        }
        writer.writeAttribute("columnNumber", "" + columnNumber, null);

        if (this.isHideColumn(table, column)) {
            writer.writeAttribute("style", "display:none", null);
        }

        if (column.isSortColumnEnabled() && table.getModel() != null && ajaxRequest != null) {
            writer.writeAttribute("onclick", ajaxRequest.createJavaScriptCall("sort_" + columnNumber, table.isAjaxDisableRenderRegionsOnRequest()), null);
        }

        writer.startElement("div", component);
        writer.writeAttribute("data-tooltip-identifier", this.createTooltipIdentifier(column), null);

        // render header label
        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-table-column-label", null);
        writer.writeText(column.getLabel(), null);
        writer.endElement("span");

        if (column.isSortColumnEnabled() && table.getTableSortModel() != null && ajaxRequest != null) {
            writer.startElement("span", component);
            final String tableUniqueIdentifier = StringUtils.getNotNullValue(table.getUniqueIdentifier(), table.getId());
            final String columnUniqueIdentifier = StringUtils.getNotNullValue(column.getUniqueIdentifier(), column.getId());
            final SortType sortType = table.getModel().getTableSortModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier);

            final StringBuilder sortSpanStyleClass = new StringBuilder("butter-component-table-column-sort-spinner ");

            if (sortType == SortType.ASCENDING) {
                sortSpanStyleClass.append(" " + webXmlParameters.getSortAscGlyphicon());
            } else if (sortType == SortType.DESCENDING) {
                sortSpanStyleClass.append(" " + webXmlParameters.getSortDescGlyphicon());
            } else {
                sortSpanStyleClass.append(" " + webXmlParameters.getSortUnknownGlyphicon());
            }

            writer.writeAttribute("class", sortSpanStyleClass.toString(), null);
            writer.endElement("span");

            if (tooltip != null) {
                tooltip.setFor("[data-tooltip-identifier=\"" + this.createTooltipIdentifier(column) + "\"]");
                tooltip.encodeAll(context);
            }
        }
    }

    private String createTooltipIdentifier(HtmlColumn column) {
        return column.getClientId() + "_div";
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // do nothing
    }

    @Override
    protected boolean shouldEncodeChildren(UIComponent component) {
        return false;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");
        writer.endElement("th");
    }

    private HtmlTooltip findTooltip(final UIComponent component) {
        for (UIComponent uiComponent : component.getChildren()) {
            if (uiComponent instanceof HtmlTooltip) {
                return (HtmlTooltip) uiComponent;
            }
        }

        return null;
    }

    private HtmlTable findParentTable(final UIComponent component) {
        return component instanceof HtmlTable || component == null
                ? (HtmlTable) component
                : findParentTable(component.getParent());
    }

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getTableColumnDisplayModel() != null) {
            final String tableUniqueIdentifier = StringUtils.getNotNullValue(table.getUniqueIdentifier(), table.getId());
            final String columnUniqueIdentifier = StringUtils.getNotNullValue(column.getUniqueIdentifier(), column.getId());
            final Boolean hideColumn = table.getTableColumnDisplayModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }
}
