/*
 * Copyright 2013 The Trustees of Indiana University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dataconservancy.dcs.access.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.dataconservancy.dcs.access.client.SeadApp;
import org.dataconservancy.dcs.access.client.SeadState;
import org.dataconservancy.dcs.access.client.Search;
import org.dataconservancy.dcs.access.client.Util;
import org.dataconservancy.dcs.access.client.api.UserService;
import org.dataconservancy.dcs.access.client.api.UserServiceAsync;
import org.dataconservancy.dcs.access.client.event.SearchEvent;
import org.dataconservancy.dcs.access.client.model.*;
import org.dataconservancy.dcs.access.client.ui.ResultNavigationWidget;
import org.dataconservancy.dcs.access.client.ui.SeadAdvancedSearchWidget;
import org.dataconservancy.dcs.access.client.ui.SeadSimpleSearchWidget;
import org.dataconservancy.dcs.access.shared.Constants;
import org.dataconservancy.dcs.access.ui.client.model.JsModel;

import java.util.*;

public class FacetedSearchPresenter implements Presenter {

    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

    Constants constants = new Constants();
    Display display;
    Panel facetPanel;
    Panel content;
    public static final UserServiceAsync userService =
            GWT.create(UserService.class);

    public interface Display {

        Button getSearchButton();
        Button getAddButton();
        FlexTable getButtonsTable();
        Panel getFacetContent();
        Panel getContentScrollPanel();
        Panel getContent();
    }
    public FacetedSearchPresenter(Display view)
    {
        this.display = view;
    }
    @Override
    public void bind() {

        //Binding search content
        content = this.display.getContent();


        //Binding facet content
        facetPanel = this.display.getFacetContent();
        content.clear();
        facetPanel.clear();

        final SearchEvent.Handler searchHandler = new SearchEvent.Handler() {
            @Override
            public void onMessageReceived(final SearchEvent event) {
                content.clear();
                facetPanel.clear();
                final VerticalPanel verticalPanel = new VerticalPanel();
                final HorizontalPanel middlePanel = new HorizontalPanel();
                middlePanel.setWidth("100%");
                //final Label viewData = Util.label("View Collections uploaded by you","LogoutButton");


                SearchInput searchInput = ((SearchEvent)event).getSearchInput();

                if (searchInput.getUserfields() == null || searchInput.getUserqueries() == null) {

                    final Search.UserField[] tempUserfields = new Search.UserField[0];
                    final String[] tempUserqueries = new String[0];
                    if(((SearchEvent)event).getIsAdvanced())
                        searchFacet(tempUserfields,
                                tempUserqueries, 0,
                                new String[0],new String[0]
                        );

                    middlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

                    verticalPanel.add(middlePanel);
                    if(SeadApp.isHome&&!((SearchEvent)event).getIsAdvanced())
                        verticalPanel.add(SeadApp.outerMoreLinks);
                    content.add(verticalPanel);

                    if(((SearchEvent)event).getIsAdvanced())
                        middlePanel.add(new SeadAdvancedSearchWidget(searchInput.getUserfields(), searchInput.getUserqueries()));
                    else
                        middlePanel.add(new SeadSimpleSearchWidget(searchInput.getUserfields(), searchInput.getUserqueries()));

                    return;
                }

                content.add(middlePanel);
                if(((SearchEvent)event).getIsAdvanced())
                    middlePanel.add(new SeadAdvancedSearchWidget(searchInput.getUserfields(), searchInput.getUserqueries()));
                else
                    middlePanel.add(new SeadSimpleSearchWidget(searchInput.getUserfields(), searchInput.getUserqueries()));


                //Create Search Query

                final String query = Search.createQuery(
                        searchInput.getUserfields(),
                        searchInput.getUserqueries(),
                        searchInput.getFacetField(),
                        searchInput.getFacetValue()
                );
                String facets = "";

                Iterator it = constants.facets.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry)it.next();
                    facets+=pairs.getKey()+",";
                }
                facets=facets.substring(0,facets.length()-1);

                String searchUrl = searchURL(query,
                        searchInput.getOffset(),
                        true,
                        Constants.MAX_SEARCH_RESULTS,
                        "_facet.field",
                        facets);

                //get Query results in searchResults variable
                getQueryResults(
                        searchUrl,
                        facets,
                        searchInput,
                        ((SearchEvent)event).getIsAdvanced()
                );
            }
        };
        SearchEvent.register(FacetedSearchPresenter.EVENT_BUS, searchHandler);
    }

    @Override
    public void display(Panel mainContainer, Panel facetContent, Panel headerPanel, Panel logoutPanel, Panel notificationPanel) {
        mainContainer.clear();
        mainContainer.add(this.display.getContentScrollPanel());
        bind();
        facetContent.clear();
        facetContent.add(this.facetPanel);


    }

    private void displaySearchResults(String query,
                                      JsSearchResult result, String facets, SearchInput searchInput, boolean isAdvanced) {


        content.add(Util.label("Total matches: " + result.total(),
                "SectionHeader"));

        int numpages = (int) result.total() / Constants.MAX_SEARCH_RESULTS;

        if (result.total() % Constants.MAX_SEARCH_RESULTS != 0) {
            numpages++;
        }

        int page = result.offset() / Constants.MAX_SEARCH_RESULTS;

        content.add(new ResultNavigationWidget(page, numpages,searchInput,result.total(), isAdvanced));

        Grid grid = new Grid(Constants.MAX_SEARCH_RESULTS,
                1);

        if(result.total()>0)
        {
            grid.setStylePrimaryName("FacetedResults");
//		        grid.setWidget(0, 0, Util.label("Dataset Name","SubSectionHeader"));
        }

        for (int i = 0; i < result.matches().length(); i++) {
            JsMatch m = result.matches().get(i);

            int resultrow = (i+1) % Constants.MAX_SEARCH_RESULTS;


            JsEntity entity = m.getEntity();
            FlowPanel desc = new FlowPanel();
            desc.setStyleName("SearchTable");
            grid.setWidget(resultrow, 0, desc);
            grid.getCellFormatter().addStyleName(resultrow,0,"SearchRow");

            if(i%2==0){
                grid.getCellFormatter().addStyleName(resultrow,0,"SearchRow");
            }else{
                grid.getCellFormatter().addStyleName(resultrow,0,"SearchRowAlt");
            }

            Hyperlink title;

            if (m.getEntityType().equals("file"))
                title = new Hyperlink(m.getSummaryStr(), true, SeadState.ENTITY
                        .toToken(((JsFile)m.getEntity()).getParent()));
            else
                title = new Hyperlink(m.getSummaryStr(), true, SeadState.ENTITY
                        .toToken(entity.getId()));

            title.setStyleName("my-HyperLink");
            desc.add(
                    title
            );

            if (m.getEntityType().equals("deliverableUnit")) {
                JsArray<JsCreator> creators = ((JsDeliverableUnit)m.getEntity()).getCreators();
                if(creators.length()>0)
                {
                    String authors="";
                    for(int j=0;j<creators.length();j++)
                        authors+=creators.get(j).getCreatorName();
                    desc.add(new HTML("<span class='ResultSnippet'>Author(s):</span>"+authors));
                    desc.add(new HTML("<br> "));
                }
            }


            if(entity.getAbstract()!=null)
            {
                int len =250;
                if(entity.getAbstract().length()<len)
                    len = entity.getAbstract().length();
                if(entity.getAbstract().length()>0){
                    desc.add(new HTML("<span class='ResultSnippet'>About:</span>"+entity.getAbstract().substring(0,len)+".."));
                    desc.add(new HTML("<br> "));
                }
            }


            if (!m.getContext().isEmpty()) {
                String context = m.getContext().replaceAll(
                        "\\w+\\:|\\'\\[|\\]\\'", " ");
                context = context.substring(0,context.indexOf("FacetCategory"));
                desc.add(new HTML("<span class='ResultSnippet'>" +"Appears in:</span>"+ context
                ));
                desc.add(new HTML("<br> "));
            }
        }

        grid.setWidth("100%");
        content.add(grid);
        //panel.add(new ResultNavigationWidget(page, numpages,query,facets));
    }



    private void searchFacet(final Search.UserField[] userfields,
                             final String[] userqueries, final int offset,
                             final String[] facetField,
                             final String[] facetValue
    ){
        JsonpRequestBuilder rb = new JsonpRequestBuilder();
        String query = Search.createQuery(userfields, userqueries, facetField, facetValue);
        String facets = "";

        Iterator it = constants.facets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            facets+=pairs.getKey()+",";
        }
        facets=facets.substring(0,facets.length()-1);

        String searchurl = searchURL(query, 0, false, 1,"_facet.field",
                facets);
        rb.setTimeout(100000);
        rb.requestObject(searchurl, new AsyncCallback<JsModel>() {

            public void onFailure(Throwable caught) {

                Util.reportInternalError("Viewing entity", caught);
            }

            public void onSuccess(JsModel result) {

                facetPanel.clear();
                Map<String,List<String>> facetsList = ((JsFacet)result).getFacets();

                displayFacets(new SearchInput(userfields,
                        userqueries, offset,
                        facetField,facetValue),facetsList);

            }
        });
    }

    private void displayFacets(
            final SearchInput searchInput,
            Map<String,List<String>> facets
    )
    {
        facetPanel.add(Util.label("Filter By", "GradientFacet"));
        FlexTable table = Util.createTable();
        Tree tree = new Tree();
        facetPanel.add(table);
        Iterator<Map.Entry<String,List<String>>> it = facets.entrySet().iterator();

        int[] countArray = new int[10];
        Map<String,List<String>> tmp = new HashMap<String,List<String>>(facets);
        Iterator<Map.Entry<String,List<String>>> tempIt = tmp.entrySet().iterator();

        while (tempIt.hasNext()) {

            Map.Entry<String,List<String>> pair = (Map.Entry<String,List<String>>)tempIt.next();
            if(pair.getKey()!=null){
                int index = Constants.order.get(pair.getKey());
                countArray[index] = ((List<String>)pair.getValue()).size();
                tempIt.remove();
            }
        }

        int orderIndex = 0;
        int i=0;

        while (orderIndex< Constants.displayOrder.size()) {
            List<String> tempFacets = facets.get(Constants.displayOrder.get(orderIndex));//pairs.getValue();

            TreeItem rootItem = new TreeItem();//pairs.getKey());
//	 			rootItem.setHTML("<b>By " +Constants.displayOrder.get(orderIndex)+"</b>");

            rootItem.setHTML("<b>" + Constants.displayOrder.get(orderIndex)+"</b>");

            String key ="";
            Iterator tempiterator = constants.facets.entrySet().iterator();
            while (tempiterator.hasNext()) {
                Map.Entry temppairs = (Map.Entry)tempiterator.next();
                if(temppairs.getValue().equals(Constants.displayOrder.get(orderIndex)))
                    key= (String)temppairs.getKey();
            }
            List<String> childFacets = SeadApp.selectedItems.get(key);

            int childExists =0;
            //get the right index
            int index;

            if(tempFacets!=null){
                for(int j=0;j<tempFacets.size();j++)
                {

                    String countStr = tempFacets.get(j).substring(tempFacets.get(j).lastIndexOf('(')+1,tempFacets.get(j).lastIndexOf(')'));
                    int count = Integer.valueOf(countStr);
                    if(count==0)
                        continue;
                    int facetLength =searchInput.getFacetField().length+1;
                    int flagAddFacet=1;
                    for(int k=0;k<facetLength-1;k++)
                    {
                        String facetFieldKey =null;
                        Iterator iterator = constants.facets.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry)iterator.next();
                            if(pair.getValue().equals(Constants.displayOrder.get(orderIndex)))
                            {
                                facetFieldKey=(String)pair.getKey();
                                break;
                            }
                        }

                        if(searchInput.getFacetField()[k].equalsIgnoreCase(facetFieldKey)&&searchInput.getFacetValue()[k].equalsIgnoreCase(tempFacets.get(j)))
                        {

                            flagAddFacet=0;
                        }
                    }
                    final String[] facetFieldNew;
                    final String[] facetValueNew;

                    if(flagAddFacet==1)
                    {
                        facetFieldNew = new String[facetLength];
                        facetValueNew = new String[facetLength];

                        for(int k=0;k<facetLength-1;k++)
                        {
                            facetFieldNew[k]=searchInput.getFacetField()[k];
                            facetValueNew[k] = searchInput.getFacetValue()[k];
                        }

                        Iterator iterator = constants.facets.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry)iterator.next();
                            if(pair.getValue().equals(Constants.displayOrder.get(orderIndex)))
                            {
                                facetFieldNew[facetLength-1]=(String)pair.getKey();
                                break;
                            }
                        }
                        facetValueNew[facetLength-1]=tempFacets.get(j);
                    }
                    else
                    {
                        facetFieldNew = new String[facetLength-1];
                        facetValueNew = new String[facetLength-1];

                        for(int k=0;k<facetLength-1;k++)
                        {
                            facetFieldNew[k]=searchInput.getFacetField()[k];
                            facetValueNew[k] = searchInput.getFacetValue()[k];
                        }
                    }

                    CheckBox checkBox;

                    FlexTable smallTable;
                    Label lbl ;
                    if(Constants.displayOrder.get(orderIndex).equals("metadata standard")&&tempFacets.get(j).contains("fgdc"))
                    {
                        String labelValue = tempFacets.get(j);
                        labelValue = labelValue.substring(labelValue.lastIndexOf('('), labelValue.lastIndexOf(')')+1);
                        //labelValue = "FGDC"+labelValue;type filter text
                        lbl = Util.label("FGDC", "FacetHyperlink");
                        Label countLbl = new Label(" ("+countStr+")");
                        smallTable = Util.createTable();
                        smallTable.setWidget(0, 0, lbl);
                        smallTable.setWidget(0, 1, countLbl);


                        checkBox = new CheckBox("FGDC" + " ("+countStr+")");
                        String facetString =tempFacets.get(j).substring(0,tempFacets.get(j).lastIndexOf('('));

                        if(childFacets!=null)
                            if(childFacets.contains(facetString))
                                checkBox.setValue(true);
                        checkBox.setName(facetString);
                        rootItem.addItem(checkBox);
                        rootItem.setState(false);

                    }
                    else
                    {
                        String facetString =tempFacets.get(j).substring(0,tempFacets.get(j).lastIndexOf('('));
                        if(facetString.length()==0){
                            continue;
                        }
                        lbl = Util.label(facetString, "FacetHyperlink");
                        Label countLbl = new Label(" ("+countStr+")");
                        smallTable = Util.createTable();
                        smallTable.setWidget(0, 0, lbl);
                        smallTable.setWidget(0, 1, countLbl);
                        checkBox = new CheckBox(facetString + " ("+countStr+")");
                        checkBox.setName(facetString);
                        if(childFacets!=null)
                            if(childFacets.contains(facetString))
                                checkBox.setValue(true);
                        rootItem.addItem(checkBox);
                        rootItem.setState(false);

                    }
                }

                tree.addItem(rootItem);
            }
            orderIndex++;

            //it.remove();
            //change the display later
        }
        tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent event) {
                TreeItem item = (TreeItem) event.getSelectedItem();
                String key = "";
                Iterator iterator = constants.facets.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pairs = (Map.Entry)iterator.next();
                    String value = (String) pairs.getValue();
                    String itemText = item.getParentItem().getText();
                    if(value.equals(itemText))
                        key= (String)pairs.getKey();
                }



                if(((CheckBox)item.getWidget()).getValue())
                {
                    //unselected
                    List<String> children = SeadApp.selectedItems.get(key);
                    children.remove(((CheckBox)item.getWidget()).getName());
                    if(children.size()==0)
                        SeadApp.selectedItems.remove(key);
                    else
                        SeadApp.selectedItems.put(key, children);
                }
                else
                {
                    List<String> children = SeadApp.selectedItems.get(key);
                    if(children==null)
                        children = new ArrayList<String>();
                    children.add(((CheckBox)item.getWidget()).getName());
                    SeadApp.selectedItems.put(key, children);
                }

                int totalFacets = 0;
                iterator = SeadApp.selectedItems.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pairs = (Map.Entry)iterator.next();
                    List<String> facetValues = (List<String>)pairs.getValue();
                    totalFacets += facetValues.size();
                }
                String[] data = new String[searchInput.getUserfields().length*2+totalFacets*2+2];

                int i =0;
                int index =-1;
                for(i=0;i<searchInput.getUserfields().length;i+=2){
                    data[++index] = searchInput.getUserfields()[i].name();
                    data[++index] = searchInput.getUserqueries()[i];
                }

                iterator = SeadApp.selectedItems.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pairs = (Map.Entry)iterator.next();
                    List<String> facetValues = (List<String>)pairs.getValue();
                    for(String facetValue : facetValues){
                        data[++index] = (String)pairs.getKey();
                        data[++index] =  facetValue;
                    }
                }

                data[++index] = String.valueOf(searchInput.getOffset());

                data[++index] = String.valueOf(totalFacets);


                History.newItem(SeadState.SEARCH.toToken(data));

            }
        });

        facetPanel.add(tree);
        //stop tree
    }

    private void getQueryResults(final String searchUrl,
                                 final String facets, final SearchInput searchInput, final boolean isAdvanced)
    {
        JsonpRequestBuilder rb = new JsonpRequestBuilder();
        rb.setTimeout(100000);
        rb.requestObject(searchUrl, new AsyncCallback<JsModel>() {

            public void onFailure(Throwable caught) {
                Util.reportInternalError("Searching", caught);
            }

            public void onSuccess(JsModel result) {
                displaySearchResults(searchUrl, (JsSearchResult)result, facets, searchInput, isAdvanced);

                Map<String,List<String>> facetsList = //JsFacet.getFacets();
                        ((JsFacet)result).getFacets();

                displayFacets(
                        searchInput,
                        facetsList
                );
            }
        });

    }

    public static String searchURL(String query, int offset, boolean context,
                                   int max, String... params) {

        String decodedQuery = URL.decode(query); //prevent double encoding (Firefox already encodes)
        String	encodedQuery = URL.encodeQueryString(decodedQuery);

        String s =
                SeadApp.accessurl+
                        SeadApp.queryPath+"?q=" + encodedQuery
                        + "&offset=" + offset + "&max=" + max;

        if (context) {
            s= s + "&_hl=true&_hl.requireFieldMatch=true&_hl.fl="
                    + URL.encodeQueryString("*");
        }
        for (int i = 0; i < params.length;i+=2) {
            s=s+"&"+params[i]+"="+params[i+1];
        }
        return s;
    }
}
