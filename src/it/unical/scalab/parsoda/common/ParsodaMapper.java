package it.unical.scalab.parsoda.common;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ParsodaMapper extends Mapper<LongWritable, Text, TextTuple, Text> {

	protected Predicate<AbstractGeotaggedItem> itemFilter = null;
	protected ItemMapper itemMapper = null;
	private TextTuple outputKey = new TextTuple();
	private Text outputValue = new Text();
	private String mapperGroupSortKey = "";
	private String mapperGroupKey = "";
	@SuppressWarnings("unused")
	private URI[] distributedFiles = null;
	private Context context = null;
	private AbstractGeotaggedItem item = null;
	private int numReducerTasks = 1;
	private MapperWriter writer = new StandardMapperWriter();

	@Override
	protected void setup(Mapper<LongWritable, Text, TextTuple, Text>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		this.context = context;

		String[] filterFunctions = context.getConfiguration().getStrings("filterFunctions");
		String[] filterParams = context.getConfiguration().getStrings("filterParams");
		String[] mapFunctions = context.getConfiguration().getStrings("mapFunctions");
		String[] mapParams = context.getConfiguration().getStrings("mapParams");
		String mapWriterFunction = context.getConfiguration().get("mapperWriter");
		String mapWriterParams = context.getConfiguration().get("mapperWriterParams");
		
		mapperGroupKey = (context.getConfiguration().get("mapperGroupKey") == null) ? ""
				: context.getConfiguration().get("mapperGroupKey");
		mapperGroupSortKey = (context.getConfiguration().get("mapperGroupSortKey") == null) ? ""
				: context.getConfiguration().get("mapperGroupSortKey");

		if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
			distributedFiles = context.getCacheFiles();
		}
		numReducerTasks = context.getNumReduceTasks();
		initFilters(filterFunctions, filterParams);
		initMappers(mapFunctions, mapParams);
		initMapperWriter(mapWriterFunction,mapWriterParams);

	}

	public void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {

		item = GeotaggedItemBuilder.create(value.toString());

		if (item == null)
			return;

		if (itemFilter != null && !itemFilter.test(item))
			return;

		if (itemMapper != null) {
			// Item transformed by Map Functions
			item = itemMapper.map(item);

			if (item == null)
				return;

			if (mapperGroupKey.equals("") && mapperGroupSortKey.equals("")) {
				// GroupKey not defined
				outputKey.set("" + key.get() % numReducerTasks, key.toString());
			} else if (item.search(mapperGroupKey) == null || item.search(mapperGroupSortKey) == null) {
				// The item has not the required groupkey
				return;
			} else {
				// <GroupKey, SortKey> for SecondarySortMethod
				outputKey.set(item.search(mapperGroupKey).toString(), item.search(mapperGroupSortKey).toString());
				// Serialize value in JSON format
				outputValue.set(item.toString());
				
				writer.write(context, outputKey, outputValue);
			}
		} else {
			if (mapperGroupKey.equals("") && mapperGroupSortKey.equals("")) {
				outputKey.set("" + key.get() % numReducerTasks, key.toString());
			} else {
				if (item.search(mapperGroupKey) != null && item.search(mapperGroupSortKey) != null) {
					outputKey.set(item.search(mapperGroupKey).toString(), item.search(mapperGroupSortKey).toString());
				}else {
					outputKey.set(mapperGroupKey, mapperGroupSortKey);					
				}
			}
			outputValue.set(item.toString());
			writer.write(context, outputKey, outputValue);
		}

	}

	private void initFilters(String[] filterFunctions, String[] params) {

		List<Pair<String, String>> list = new LinkedList<Pair<String, String>>();
		if (filterFunctions != null) {
			for (int i = 0; i < filterFunctions.length; i++) {
				if (params == null || params[i] == null || params[i].trim().length()==0) {
					list.add(new Pair<String, String>(filterFunctions[i], ""));
				} else {
					list.add(new Pair<String, String>(filterFunctions[i], params[i]));
				}
			}

			if (list.size() > 0) {
				itemFilter = list.stream().map(e -> createFilterFunction(e)).filter(Objects::nonNull).reduce(e -> true,
						Predicate::and);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate<AbstractGeotaggedItem> createFilterFunction(Pair<String, String> p) {
		try {

			Class<AbstractFilterFunction> _tempClass = (Class<AbstractFilterFunction>) Class
					.forName(p.getLeft().trim());
			AbstractFilterFunction function = null;
			if (p.getRight() != null && p.getRight().trim().length() > 0) {
				Constructor<AbstractFilterFunction> ctor = _tempClass.getConstructor(String.class);
				function = (AbstractFilterFunction) ctor.newInstance(p.getRight());
			} else {
				Constructor<AbstractFilterFunction> ctor = _tempClass.getConstructor();
				function = ctor.newInstance();
			}
			return (Predicate<AbstractGeotaggedItem>) function;
		} catch (IllegalArgumentException | InvocationTargetException | InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void initMapperWriter(String mapWriterFunction, String mapWriterParams) {
		if (mapWriterFunction != null && !mapWriterFunction.equals("")) {
			try {

				Class<MapperWriter> _tempClass = (Class<MapperWriter>) Class.forName(mapWriterFunction);
				if (mapWriterParams.length() > 0) {
					Constructor<MapperWriter> ctor = _tempClass.getConstructor(String.class);
					this.writer = (MapperWriter) ctor.newInstance(mapWriterParams);
				} else {
					Constructor<MapperWriter> ctor = _tempClass.getConstructor();
					this.writer = ctor.newInstance();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void initMappers(String[] mapFunctions, String[] params) {

		List<Pair<String, String>> list = new LinkedList<Pair<String, String>>();
		if (mapFunctions != null) {
			for (int i = 0; i < mapFunctions.length; i++) {
				if (params == null || params[i] == null) {
					list.add(new Pair<String, String>(mapFunctions[i], ""));
				} else {
					list.add(new Pair<String, String>(mapFunctions[i], params[i]));
				}
			}

			if (list.size() > 0) {
				this.itemMapper = new ItemMapper(list.stream().map(e -> createMapFunction(e)).filter(Objects::nonNull)
						.collect(Collectors.toList()));
			}
		}

	}

	@SuppressWarnings("unchecked")
	private AbstractMapFunction createMapFunction(Pair<String, String> p) {
		try {
			Class<AbstractMapFunction> _tempClass = (Class<AbstractMapFunction>) Class.forName(p.getLeft().trim());
			AbstractMapFunction function = null;
			if (p.getRight() != null && p.getRight().trim().length() > 0) {
				Constructor<AbstractMapFunction> ctor = _tempClass.getConstructor(String.class);
				function = (AbstractMapFunction) ctor.newInstance(p.getRight());
			} else {
				Constructor<AbstractMapFunction> ctor = _tempClass.getConstructor();
				function = ctor.newInstance();
			}
			function.setContext(context);
			return (AbstractMapFunction) function;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
