package com.elastic.demo.Controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.filter.Filter;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation.Builder.ContainerBuilder;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.BucketAggregationBase;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import co.elastic.clients.elasticsearch._types.aggregations.NestedAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldAndFormat;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.InnerHits;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.json.jackson.JacksonJsonProvider;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.util.NamedValue;
import io.micrometer.common.util.StringUtils;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class VehicleService {
	@Autowired
	private ElasticsearchClient client;
	
	private static final String index="phasezero_ymme";
	private static final String part_index="phasezero_part";
	
	public record MyResponse(String id,String score, double price) { }
	
	public List<Object> getVehicles() throws ElasticsearchException, IOException{
		
	 StringWriter writer = new StringWriter();
	  Query termQueryVipar= TermQuery.of(t->t.field("catalog").value("VIPAR_NA"))._toQuery();
      
	  List<Query> viparboolList=new ArrayList<>();
	  viparboolList.add(termQueryVipar);
	  List<Query> partBoolList=List.of(
			  TermQuery.of(terms->terms.field("part_type").value("MODEL"))._toQuery(),
			  TermQuery.of(terms->terms.field("part_type").value("PART"))._toQuery(),
			  TermQuery.of(terms->terms.field("part_type").value("SOFTWARE"))._toQuery());
	  
	 	  
	
	  List<Query> listQuery=List.of(TermQuery.of(terms->terms.field("status").value(1))._toQuery(),
			  new BoolQuery.Builder().should(viparboolList).build()._toQuery(),
			  new BoolQuery.Builder().should(partBoolList).build()._toQuery());
	  
	 
	  BoolQuery.Builder queryBuilder = new BoolQuery.Builder();
	  /*
	           Construction of multi match query
	   * */
	  
	  List<String> listOfMatchFields =List.of("attr_name_values^1.1",
              "cat_name.name_edgengram^40.1",
              "long_desc^25.1",
              "long_desc.long_desc_edgengram^20.1",
              "origional_part_number.origional_part_number^50000.1",
              "part_desc^35.1",
              "part_desc.part_desc_edgengram^20.1",
              "part_number.part_number_edgengram^120.1",
              "part_number.part_number_ngram^120.8");
	         
	  List<Query>  list_multi_nest_query = new ArrayList<Query>();
	  
	  
	  Query multiMatchQuery=
	  MultiMatchQuery.of(multi->multi.query("Audi").fields(listOfMatchFields)
			  .type(TextQueryType.CrossFields)
			  .operator(Operator.Or).maxExpansions(50)
			  .autoGenerateSynonymsPhraseQuery(true).fuzzyTranspositions(true)
			  )._toQuery();
	  
	               list_multi_nest_query.add(multiMatchQuery);
	  
	  /*
	    construct the nested  query with multiMatch 
	   */
	   List<String> nestedMultiMatchFieldsList = List.
			   of("interchange.part_number.part_number_edgengram^48.1",
               "interchange.part_number.part_number_ngram^48.1",
               "interchange.part_number.part_number_trimmed^48.1");
	 
		Query multiMatchQuery2 = MultiMatchQuery.of(multi -> multi.query("Audi")
				.fields(nestedMultiMatchFieldsList)
				.type(TextQueryType.CrossFields)
				.operator(Operator.And)
				.maxExpansions(50)
				.autoGenerateSynonymsPhraseQuery(true)
				.fuzzyTranspositions(true))._toQuery();

		List<FieldAndFormat> docValueFieldslist = List.of(
				FieldAndFormat.of(f -> f.field("interchange.part_number.part_number_raw")),
				FieldAndFormat.of(f -> f.field("interchange.name")),
				FieldAndFormat.of(f -> f.field("interchange.comments")),
				FieldAndFormat.of(f -> f.field("interchange.foot_notes")),
				FieldAndFormat.of(f -> f.field("interchangetype")));
		// construction of inner hits
		InnerHits innerHits = InnerHits.of(i-> i.ignoreUnmapped(false)
				.docvalueFields(docValueFieldslist));
	   
	   
	  Query nestedQuery= NestedQuery.of(n->n.query(multiMatchQuery2)
			  .path("interchange")
			  .scoreMode(ChildScoreMode.Avg)
			  .ignoreUnmapped(false)
			  .innerHits(innerHits))._toQuery();
	  
	  List<Query> sholdQueryList=List.of(multiMatchQuery,nestedQuery);
	  
	  
	 
	  
	 // below Query is the main appended query for part_index
	 Query partsQuery= queryBuilder.must(listQuery)
			 .should(sholdQueryList)
			 .build()._toQuery();

	  
		//constructing the aggregation
		
		List<NamedValue<SortOrder>>  orderFieldsList= 
				List.of(new NamedValue<SortOrder>("_count", SortOrder.Desc),
				new NamedValue<SortOrder>("_key", SortOrder.Asc));
		
		
		Map<String, Aggregation> aggMap = new HashMap<>();
		TermsAggregation termAggregation= new TermsAggregation.Builder()
				.field("cat_hierarchy").order(orderFieldsList).build();
		
		
		
		Aggregation agg=new Aggregation.Builder()
				.terms(termAggregation).build();
		aggMap.put("category_list", agg);
		
		//build source 
		
		 Map<String, Query> aggFilterMap = new HashMap<>();
		 aggFilterMap.put("agg", BoolQuery.of(b->b.should(partBoolList))._toQuery());
		 FiltersAggregation	 fAgg=
	    FiltersAggregation.of(filterAgg->filterAgg.filters(f->f.keyed(aggFilterMap)));
	  
//		// Bucket.
//		//Constructing a TermAggregation
//		TermsAggregation termAgg= new TermsAggregation.Builder()
//				.field("termAgggg..").build();
//		Map<String, Aggregation> aggTermAgg = new HashMap<>();
//		Aggregation aggTerm=new Aggregation.Builder()
//			.terms(termAgg).build();
//		//Aggregation.of(ai->ai.terms(termAgg));
//		aggTermAgg.put("category_Count", aggTerm);
//		Map<String, Aggregation> aggMapParent = new HashMap<>();
//		
//		
//		//Combining the above to Aggregation into a single Aggregation.
//		Aggregation aggregation=
//		Aggregation.of(a->a.filters(fAgg).aggregations(aggTermAgg));
//		aggMapParent.put("agg", aggregation);
		 
		 
		 
		 TermsAggregation termAgg= new TermsAggregation.Builder()
					.field("termAgggg..").build();
			Map<String, Aggregation> aggTermAgg = new HashMap<>();
			Aggregation aggTerm=new Aggregation.Builder()
				.terms(termAgg).build();
			aggTermAgg.put("category_Count", aggTerm);
			Map<String, Aggregation> aggMapParent = new HashMap<>();
			
			
			//Combining the above to Aggregation into a single Aggregation.
			Aggregation aggregation=
			Aggregation.of(a->a.filters(f->f.filters(b->b.array(viparboolList))).aggregations(aggTermAgg));
			aggMapParent.put("agg", aggregation);
	  
		
		// Highlight TODO

		//build source 
		List<String> excludesList=List.of("bom",
	            "customer_interchange",
	            "app",
	            "recomnd_parts",
	            "serial_numbers",
	            "related_parts",
	            "interchange");
	   SourceConfig config=
		SourceConfig.of(s->s.filter(SourceFilter.of(sf->sf.excludes(excludesList)
			)));
	   
	   
	   // DANA new filter test ..
//	   
		SearchRequest request1 = new SearchRequest.Builder().index(part_index)
				//.source(config)
				//.aggregations("agg", formatYMMAggregation())
				.aggregations(nestedAggwithFilterAggAndTermAgg())
				.query(partsQuery)
				// .highlight(highlight)
				.build();

		JsonGenerator generator1 = JacksonJsonProvider.provider().createGenerator(writer);
		request1.serialize(generator1, new JacksonJsonpMapper());
		generator1.flush();
		System.out.println("query {}:" + writer.toString());
	
		List<Object> partPojoList=new ArrayList<>();
		 
	   // SearchResponse<Object> response=client.search(request1, Object.class);
	    
	    ///  Object bucket =  response.aggregations().get("category_list");
	      
	      
	  	Set<String> responseData = new TreeSet<String>();
//	    
//	    List<Hit<Object>> hits=response.hits().hits();
//	    
//	    
//	    
//	    Map<String ,Aggregate> map=response.aggregations();
		
	    
	    
	  //  Aggregate obj=  map.get("category_list");
	    // R&D on Buckets 
//	    StringTermsAggregate b=   obj.sterms();
//	    List<StringTermsBucket> bucketList=
//	          b.buckets().array();
//	    
//	    bucketList.forEach(x->{
//	    	
//	    	responseData.add(x.key().stringValue());
//	     	    	
//	    });
//	    System.out.println("finall response------collection keys ------");
//	    responseData.stream().forEach(System.out::print);
	  
	  
	    
//	    for(Hit<Object> hit:hits){
//	    	//MyResponse(hit.id(),hit.score(),hit.score())
//	    	partPojoList.add(hit.source());
//	    	
//	    } 
	    return partPojoList;
	}

	
	
	
	
	
	
   public Map<String, Aggregation> nestedAggwithFilterAggAndTermAgg(){
	   
	Query termQueryYmme= TermQuery.of(t->t.field("app.year.year").value("2001"))._toQuery();
	Query boolQueryInsideFilter=   BoolQuery.of(b->b.must(List.of(termQueryYmme)))._toQuery();
 
	 // --------------Aggregation---------
	  
	 
	  //construct the double Term Aggregation
	  
		 TermsAggregation termAgg= new TermsAggregation.Builder()
					.field("app.year.year").build();
		 TermsAggregation termAggMake= new TermsAggregation.Builder()
					.field("app.make.make").build();
		 
		 Map<String , Aggregation> insideMakeAggregation =Map.of("make",
	      	 new Aggregation.Builder()
	        		.terms(termAggMake).build());
		
			Map<String, Aggregation> aggTermAgg = new HashMap<>();
			ContainerBuilder aggTerm=new Aggregation.Builder()
				.terms(termAgg);
			
			
			aggTermAgg.put("YEAR",  aggTerm.aggregations(insideMakeAggregation).build());
	  
	  //---construct the filter ------
	 Map<String , Aggregation> filterMap=new HashMap<String, Aggregation>();
	  Aggregation aggregation=
				Aggregation.of(a->a.filters(f->f.filters(b->b.array(List.of(boolQueryInsideFilter)))));
	  filterMap.put("ymmfilter", aggregation);
	  
	  

	   //----construct the nested -----
	  Aggregation nestedAgg= new Aggregation.Builder()
			  .nested(nested->nested.path("app")).aggregations(filterMap)
			  .aggregations(aggTermAgg).build();
	   
	   return Map.of("ymm_list",nestedAgg);
	   
   }
	
	
	public void PostFilterBuilding() {
		StringWriter writer=new StringWriter();
		List<Query> QueryList =new  ArrayList<Query>();
	//	Builder result=new BoolQuery.Builder();
	//	Builder boolqueryBuilder =null;
//		filterByCatalog(boolqueryBuilder,List.of("pzv","OPTIMA"));
//		filterByCatalog2(boolqueryBuilder,List.of("pzv2","VARTA"));
		
	  //  Builder qe=	result.must(List.of(result.build()._toQuery()));
	    
	    
	   //   SortOptions sortOption= SortOptions.of(x-> x.field(FieldSort.of(f->f.field("brand")).order()));
		List<String> catlalogs =List.of("amaoma","akma","ajajajjaja");
		
		List<FieldValue> valList = new ArrayList<FieldValue>();
		catlalogs.forEach(catalog -> {
			valList.add(FieldValue.of(catalog));
		});
		TermsQueryField termsQueryField = TermsQueryField.of(values -> values.value(valList));
		Query q1=
		TermsQuery.of(terms->terms.field("PHASEZERO").terms(termsQueryField))._toQuery();
		
		
		
		
		
	// printing the post filter in the search request 
		List<FieldValue> catalogList =new ArrayList<FieldValue>();
		List<String> offhighwayCatalogs=List.of("sandeep","phasezero");
		offhighwayCatalogs.parallelStream().filter(c->!ObjectUtils.isEmpty(c)).forEach(catalog -> {
			catalogList.add(FieldValue.of(catalog));
		});
		
	
	
		TermsQueryField termsQueryField2 = TermsQueryField.of(values -> values.value(catalogList));
		
		Query termQueryMust=
				TermsQuery.of(terms->terms.field("partCriterionEnum").terms(termsQueryField2))._toQuery();
	
		Query boolQueryshould=
				BoolQuery.of(b->b.mustNot(TermQuery.of(term->term.field("partraw").value("1212"))._toQuery()))._toQuery();
		
		Query nestedQueryMust=
		NestedQuery.of(nest->nest.query(BoolQuery.of(prefixBool->prefixBool
				 .must(PrefixQuery.of(p->p.field("prefixfield").value("1212"))._toQuery()))._toQuery())
				 .path("nestedQueryPath").scoreMode(ChildScoreMode.Min))._toQuery();
		
		Query query=
		BoolQuery.of(b->b.must(List.of(termQueryMust,nestedQueryMust)).should(List.of(boolQueryshould)))._toQuery();
		
		SearchRequest request1 = new SearchRequest.Builder().index(part_index)
				.query(q1).postFilter(BoolQuery.of(b->b.mustNot(query))._toQuery())
				.build();
		
		JsonGenerator generator1 = JacksonJsonProvider.provider().createGenerator(writer);
		request1.serialize(generator1, new JacksonJsonpMapper());
		generator1.flush();
		System.out.println("query {}:" + writer.toString());
		
	}
	
	
	
	
	
	
	
	protected void filterByCatalog(Builder boolQueryBuilder, List<String> catalogs) {
		
		Builder queryBuilder = new BoolQuery.Builder();
	
		      List<Query> termsQuerylist = new ArrayList<Query>();
				for (String catalog : catalogs) {
			termsQuerylist.add(TermQuery.of(term->term.field("partType").value(catalog))._toQuery());
		}
				queryBuilder.should(termsQuerylist);
		queryBuilder.minimumShouldMatch("1");
		// boolQueryBuilder.must(List.of(queryBuilder)); 
	} 
	
	protected void filterByCatalog2(Builder boolQueryBuilder, List<String> catalogs) {
		Builder queryBuilder = new BoolQuery.Builder();
		List<Query> termList = new ArrayList<Query>();
		for (String catalog : catalogs) {
			termList.add(
					TermQuery.of(term->term.field("partCatalogString")
							.value(catalog))._toQuery());
		}
		queryBuilder.should(termList);
		queryBuilder.minimumShouldMatch("1");
		//boolQueryBuilder.must(queryBuilder);
	}


	public void printQuery() {
		// TODO Auto-generated method stub
		
	}
	
	
	  public static List<String> extractKeysFromJson(String jsonData) {
	        List<String> keys = new ArrayList<>();

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(jsonData);
 
	            JsonNode bucketsNode = rootNode.get("buckets");
	            if (bucketsNode.isArray()) {
	                for (JsonNode bucket : bucketsNode) {
	                    String key = bucket.get("key").asText();
	                    keys.add(key);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return keys;
	    }
	  
	  
//	  
//	  private void agg() {
////
////				  FiltersAggregation filtersAggregation = AggregationBuilders.
////						  filters("aggs", 
////								  BoolQuery.of(b->b.must(List.of(TermQuery.of(t->t.field("y filed")
////										  .value("hey"))._toQuery())))._toQuery());
//				  
//		  Buckets<Query> outerBoolQuery=
//				  Buckets.of(x->x.array(List.of( BoolQuery.of(b->b.must(List.of(TermQuery.of(t->t.field("y filed")
//				  .value("hey"))._toQuery())))._toQuery())))
//				 ;
//		  
//	
//
//		  
//			TermsAggregation termAgg= new TermsAggregation.Builder()
//					.field("termAgggg").build();
//			Map<String, Aggregation> aggTermAgg = new HashMap<>();
//			Aggregate aggTerm=new Aggregation.Builder()
//				.terms(termAgg).build();
//			//Aggregation.of(ai->ai.terms(termAgg));
//			
//			
////			  Aggregation filtersAggregation = AggregationBuilders.filters(builder ->
////	          builder.filters( outerBoolQuery)
////	  );
//			//  aggTermAgg.put("agg", filtersAggregation);
//			
//			;
//			FiltersAggregate.of(f->f.buckets(FiltersBucket.of(x->x.aggregations("category_Count", aggTerm))));
//		
//		//	FiltersAggregation.of(f->f.filters(outerBoolQuery));
//
//			  aggTermAgg.put("category_Count", aggTerm);
  

////			Aggregation finalAgg=new Aggregation.Builder()
////					.aggregations("agg", filtersAggregation)
//		  
//	        // Add a sub-aggregation
////	        TermsAggregation termsAggregation = AggregationBuilders.terms("CATEGORY_COUNT")
////	                .field("CATEGORY_HIERARCHY").size("slma");
////	        filtersAggregation.subAggregation(termsAggregation);
//	  }
	  
	  
	  
	  //Dana verification snippet OffHighway filter
	  public Map<String ,Aggregation> addCategoryAggFilter( String partNumber,
				Boolean offhighwayUserSearchPolicy) {
			

			Map<String ,Aggregation> aggregationMap=new HashMap<>();;
			// termAggregation is common for both if and else block .  
			TermsAggregation termAgg= new TermsAggregation.Builder()
					.field("cat_hierarchy").build();
			Aggregation aggTerm=new Aggregation.Builder()
				.terms(termAgg).build();
		
			if(!StringUtils.isEmpty(partNumber) && !offhighwayUserSearchPolicy) {
				Map<String, Aggregation> aggTermAgg = new HashMap<>();
				final Query categoryAggFilter=getOffHighWayCatologFilter(partNumber);
				aggTermAgg.put("category_count", aggTerm);
				    Aggregation aggregation=
					Aggregation.of(a->a.filters(f->f.filters(b->
					b.array(List.of(categoryAggFilter)))).aggregations(aggTermAgg));
				    aggregationMap.put("ymm_list", aggregation);
					
			}else {
				aggregationMap.put("category_count", aggTerm);
			}
			return aggregationMap;
		}

		
		private Query getOffHighWayCatologFilter(String partNumber) {
			Query filterHighWayCatalog = null;

			

				List<FieldValue> catalogList = new ArrayList<FieldValue>();
				// verify with team null check require or not .. below
				List.of("KB_OH_SAP").parallelStream().filter(c -> !ObjectUtils.isEmpty(c)).forEach(catalog -> {
					catalogList.add(FieldValue.of(catalog));
				});
				TermsQueryField termsQueryField = TermsQueryField.of(values -> values.value(catalogList));
				Query termQueryMust = TermsQuery
						.of(terms -> terms.field("catalog").terms(termsQueryField))
						._toQuery();

				Query boolQuery = BoolQuery.of(b -> b.mustNot(TermQuery
						.of(term -> term.field( "part_number.part_number_raw").value(partNumber))
						._toQuery()))._toQuery();

				Query nestedQueryMust = NestedQuery
						.of(nest -> nest
								.query(BoolQuery
										.of(prefixBool -> prefixBool.must(
												PrefixQuery.of(p -> p.field(partNumber).value(partNumber))._toQuery()))
										._toQuery())
								.path("interchange.part_number.part_number_raw").scoreMode(ChildScoreMode.Min))
						._toQuery();

				Query resultQuery = BoolQuery.of(b -> b.must(List.of(termQueryMust, nestedQueryMust)).should(boolQuery))
						._toQuery();
				// Build a mustNot on top of resultQuery
				filterHighWayCatalog = BoolQuery.of(b -> b.mustNot(resultQuery))._toQuery();
			
			return filterHighWayCatalog;
		}
		
	
		
		
		public Aggregation formatYMMAggregation() {
			
			
				TermsAggregation parentBuilder = null;
				//BoolQueryBuilder appQueryBuilder = QueryBuilders.boolQuery();
				
				YMMType ymmType = YMMType.valueOf("AUTO");
				ContainerBuilder ymmAggrBuilder=null;
				Query  appQueryBuilder=null;
				Map<String, Aggregation> aggTermAgg = new HashMap<>();
				Aggregation filterAggegation=null;
				Map<String, Aggregation>  populateYmmAggMap = null;
				
				if (ymmType != null) {
					ymmAggrBuilder=new Aggregation.Builder().nested(NestedAggregation.of(n->n.path("app")));
					List<String> ymmConfig = List.of("YEAR", "MAKE", "MODEL", "ENGINE");
					//ask ymmCofig size differs
			
					int requestLvlSize = 0;
					if (!CollectionUtils.isEmpty(List.of("2011", "Alfa Romeo", "159 (939_)", "1.8 MPI (939AXL1A) (103 kW / 140 PS)"))) {
						requestLvlSize = List.of("2011", "Alfa Romeo", "159 (939_)", "1.8 MPI (939AXL1A) (103 kW / 140 PS)").size();
					}
					for (int i = 0; i < ymmConfig.size() && i <= requestLvlSize; i ++) {
						YMMEnum ymmEnum = YMMEnum.findBykey(ymmConfig.get(i));

						String value = getYMMValueFromRequest(ymmEnum, ymmConfig,
								List.of("2011", "Alfa Romeo", "159 (939_)", "1.8 MPI (939AXL1A) (103 kW / 140 PS)"));
						
						boolean valueFound = !StringUtils.isEmpty(value);
						 TermsAggregation termAgg= new TermsAggregation.Builder()
									.field(getSearchCriteriaEnum(ymmEnum).toString()).build();
							
//							Aggregation aggTerm=new Aggregation.Builder()
//								.terms(termAgg).build();
							
						
																		
						if (valueFound) {
							 appQueryBuilder=	
							BoolQuery.of(b->b.must(List.of(TermQuery.of(t->t.field(getSearchCriteriaEnum(ymmEnum).toString())
									.value(value))._toQuery())))._toQuery();
						}
							
						if (parentBuilder == null) {
							parentBuilder = termAgg; 
							if (requestLvlSize == 0) {
								//ymmAggrBuilder.subAggregation(parentBuilder);
								
								//since we dont have the subAggregation we are adding externally using the map..
								Aggregation aggTerm=new Aggregation.Builder()
										.terms(termAgg).build();
								aggTermAgg.put(ymmEnum.name(), aggTerm);
					               populateYmmAggMap=populateYMMAggregation(ymmConfig, i);
					               aggTermAgg.putAll(populateYmmAggMap);
					               ymmAggrBuilder.aggregations(aggTermAgg);
					              
							} else {
								final Query appQueryBuilderfinal=appQueryBuilder;
										Aggregation.of(a->a.filters(f->f.filters(filter->
										filter.array(List.of(appQueryBuilderfinal)))).aggregations(aggTermAgg));
								 aggTermAgg.put(ymmEnum.name(), filterAggegation);
								 
							 ymmAggrBuilder.aggregations(aggTermAgg);
							}
						} 
						
						
						
						
//						else {
//							//addSubAggregation(parentBuilder, builder);
//						}
					}		
							
				}
				return ymmAggrBuilder.build() ; 
			}

			private Map<String, Aggregation> populateYMMAggregation(List<String> ymmConfig, int i) {
				
				//TODO .size(YMM_TERM_AGGR_SIZE) .order(BucketOrder.key(true));		
				Map<String, Aggregation> aggTermAgg = new HashMap<>();
				
				for (int j = i + 1; j < ymmConfig.size(); j++) {
					YMMEnum ymmenum = YMMEnum.findBykey(ymmConfig.get(j));
					 TermsAggregation termAgg= new TermsAggregation.Builder()
								.field(getSearchCriteriaEnum(ymmenum).toString()).build();
						Aggregation aggTerm=new Aggregation.Builder()
							.terms(termAgg).build();
						aggTermAgg.put(ymmenum.name(), aggTerm);
				}
				
				return  aggTermAgg;
			}
			
			
			protected PartSearchCriteriaEnum getSearchCriteriaEnum(YMMEnum ymmEnum) {
				PartSearchCriteriaEnum searchEnum = null;
				switch (ymmEnum) {
					case YEAR:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_YEAR;
						break;
					case MAKE:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_MAKE;
						break;
					case MODEL:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_MODEL;
						break;
					case ENGINE:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_ENGINE_DESC;
						break;
					case CATEGORY:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_CATEGORY;
						break;
					case SUBCATEGORY:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_SUBCATEGORY;
						break;
					case TYPEVIN:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_TYPE_INDICATION_VIN;
						break;
					case DRIVE_TYPE:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_DRIVETYPE;
						break;
					case LOCATOR_IMAGE:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_IMGS_FILE;
						break;
					case LOCATOR_LOC_NO:
						searchEnum = PartSearchCriteriaEnum.APPLICATION_IMGS_LOCNO;
						break;
					case YOKE_FLANGE_APP:
						searchEnum = PartSearchCriteriaEnum.YOKE_FLANGE_APP;
						break;
					case YOKE_FLANGE_MAKE:
						searchEnum = PartSearchCriteriaEnum.YOKE_FLANGE_MAKE;
						break;
					case YOKE_FLANGE_MODEL:
						searchEnum = PartSearchCriteriaEnum.YOKE_FLANGE_MODEL;
						break;
					case KING_PIN_MAKE:
						searchEnum = PartSearchCriteriaEnum.KING_PIN_MAKE;
						break;
					case KING_PIN_MODEL:
						searchEnum = PartSearchCriteriaEnum.KING_PIN_MODEL;
						break;
					default:
						break;
				}
				return searchEnum;
			}
			
			
			protected String getYMMValueFromRequest(YMMEnum ymm, List<String> ymmConfig, List<String> lvl) {
				
				if (!CollectionUtils.isEmpty(lvl)){
					for (int i = 0; i < ymmConfig.size() && i < lvl.size(); i++) {
						if (ymm.name().equals(ymmConfig.get(i))) {
							return lvl.get(i);
						}
					}
				}
				return null;
			}
			
			
			
			
		
		
		
		
		
}	
