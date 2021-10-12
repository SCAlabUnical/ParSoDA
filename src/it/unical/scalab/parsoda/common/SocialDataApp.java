package it.unical.scalab.parsoda.common;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SocialDataApp implements Tool {

    private List<Job> jobs = new LinkedList<Job>();
    private boolean jobSuccessful = false;
    ;
    private Collection<String> distributedCacheFiles = new LinkedList<String>();
    private Configuration conf = null;
    private boolean parallelJobs = true;
    private HashMap<String, Class> inputBasePath = new HashMap<String, Class>();
    private int numReducer = 1;
    private String outputBasePath = "outputApp";
    private String jobName = "Tags Analysis";
    private Class<? extends Reducer> reducerClass = ParsodaReducer.class;
    private Class<? extends Mapper> mapperClass = ParsodaMapper.class;
    private AbstractAnalysisFunction analysisFunction = null;
    private AbstractVisualizationFunction visualizationFunction = null;
    private List<AbstractCrawlerFunction> crawlers = new LinkedList<AbstractCrawlerFunction>();

    public SocialDataApp(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public Configuration getConf() {
        if (conf == null) {
            conf = new Configuration();
        }
        return conf;
    }

    public void setLocatFileSystem() {
        getConf().set("fs.defaultFS", "file:///");
    }

    public void setHDFS(String host, String port) {
        getConf().set("fs.defaultFS", "hdfs://" + host + ":" + port);
    }

    public void setFileSystem(String path) {
        getConf().set("fs.defaultFS", path);
    }

    public List<AbstractCrawlerFunction> getCrawlers() {
        return crawlers;
    }

    public void setCrawlers(Class<AbstractCrawlerFunction>[] cFunctions, String[] cParams) {
        for (int i = 0; i < cFunctions.length; i++) {
            try {
                AbstractCrawlerFunction function = null;

                Class<AbstractCrawlerFunction> _tempClass = cFunctions[i];
                if (cParams != null && cParams[i].length() > 0) {
                    Constructor<AbstractCrawlerFunction> ctor = _tempClass.getConstructor(String.class);
                    function = (AbstractCrawlerFunction) ctor.newInstance(cParams[i]);
                } else {
                    Constructor<AbstractCrawlerFunction> ctor = _tempClass.getConstructor();
                    function = ctor.newInstance();
                }

                function.setBasePath(this.outputBasePath+ "/");
                function.setInputPath(this.outputBasePath);
                function.setOutputPath(this.outputBasePath + "-acquisition");
                function.setFileSystem(FileSystem.newInstance(this.getConf()));
                this.crawlers.add(function);

            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMapperWriter(Class mwFunction, String mwParams) {
        this.getConf().set("mapperWriter", mwFunction.getCanonicalName());
        if (mwParams != null && mwParams.length() > 0)
            this.getConf().set("mapperWriterParams", mwParams);
    }

    public void setReducerClass(Class<? extends Reducer> reducerClass) {
        this.reducerClass = reducerClass;
    }

    public String getOutputBasePath() {
        return outputBasePath;
    }

    public Class<? extends Mapper> getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class<? extends Mapper> mapperClass) {
        this.mapperClass = mapperClass;
    }

    public void setOutputBasePath(String outputBasePath) {
        this.outputBasePath = outputBasePath;
    }

    public int getNumReducer() {
        return numReducer;
    }

    public void setNumReducer(int numReducer) {
        this.numReducer = numReducer;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public int run(String[] arg0) throws Exception {

        createJobs();

        for (Job jobItem : jobs) {
            jobSuccessful = jobItem.waitForCompletion(!parallelJobs);
            if (!parallelJobs) {
                System.out.println("Job ID=" + jobItem.getJobID().getId() + " finished with status: " + jobSuccessful);
            }
        }
        return 0;
    }

    public void addInputPath(String path, Class c) {
        this.inputBasePath.put(path, c);
    }

    public void addInputPath(String path) {
        this.inputBasePath.put(path, TextInputFormat.class);
    }

    protected void createJobs() {

        Job job = null;
        try {
            job = Job.getInstance(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set distributed cache files
        for (String file : distributedCacheFiles) {
            job.addCacheFile(new Path(file).toUri());
        }

        job.setJobName(jobName);
        job.setReducerClass(reducerClass);
        job.setNumReduceTasks(numReducer);
        job.setJarByClass(this.getClass());

        // Settiamo formati di Input e di Output
        job.setMapOutputKeyClass(TextTuple.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // Set Partitioner, Comparator and Sorting
        job.setPartitionerClass(SecondarySort.SSPartitioner.class);
        job.setGroupingComparatorClass(SecondarySort.SSGroupComparator.class);
        job.setSortComparatorClass(SecondarySort.SSSortComparator.class);
        job.setJarByClass(this.getClass());

        TextOutputFormat.setOutputPath(job, new Path(outputBasePath));

        if (this.crawlers.size() > 0) {
            for (AbstractCrawlerFunction entry : crawlers) {
                this.addInputPath(entry.getResultPath());
            }
        }

        // Definizione input
        for (Entry<String, Class> entry : inputBasePath.entrySet()) {
            MultipleInputs.addInputPath(job, new Path(entry.getKey()), entry.getValue(), mapperClass);
        }

        jobs.add(job);

    }

    public void setMapFunctions(Class<AbstractMapFunction>[] mapFunctions, String[] params) {
        if (mapFunctions != null) {

            String[] functions = new String[mapFunctions.length];
            for (int i = 0; i < mapFunctions.length; i++) {
                functions[i] = mapFunctions[i].getCanonicalName();
            }

            getConf().setStrings("mapFunctions", functions);
        }
        if (params != null) {
            getConf().setStrings("mapParams", params);
        }
    }

    public void setFilters(Class<AbstractFilterFunction>[] filterFunctions, String[] params) {
        if (filterFunctions != null) {
            String[] functions = new String[filterFunctions.length];
            for (int i = 0; i < filterFunctions.length; i++) {
                functions[i] = filterFunctions[i].getCanonicalName();
            }
            getConf().setStrings("filterFunctions", functions);
        }
        if (params != null) {
            getConf().setStrings("filterParams", params);
        }
    }

    public void setReduceFunction(Class<AbstractReduceFunction> reductionFunction, String params) {
        getConf().set("reduceFunction", reductionFunction.getCanonicalName());
        if (params != null)
            getConf().set("reduceParams", params);
    }

    public void setDistributedCacheFiles(LinkedList<String> distributedCacheFiles) {
        this.distributedCacheFiles = distributedCacheFiles;
    }

    public void setDistributedCacheFiles(String[] distributedCacheFiles) {
        for (String file : distributedCacheFiles) {
            this.distributedCacheFiles.add(file);
        }
    }

    public void setParallelJobs(boolean parallelJobs) {
        this.parallelJobs = parallelJobs;
    }

    public void addDistributedCacheFile(String file) {
        this.distributedCacheFiles.add(file);
    }

    public void setPartitioningKeys(String groupKey, String groupSortKey) {
        getConf().set("mapperGroupKey", groupKey);
        getConf().set("mapperGroupSortKey", groupSortKey);
    }

    @SuppressWarnings("unchecked")
    public void setAnalysisFunction(Class<AbstractAnalysisFunction> function, String analysisParams) {
        try {
            Class<AbstractAnalysisFunction> _tempClass = function;
            if (analysisParams != null && analysisParams.length() > 0) {
                Constructor<AbstractAnalysisFunction> ctor = _tempClass.getConstructor(String.class);
                this.analysisFunction = (AbstractAnalysisFunction) ctor.newInstance(analysisParams);
            } else {
                Constructor<AbstractAnalysisFunction> ctor = _tempClass.getConstructor();
                this.analysisFunction = ctor.newInstance();
            }
            this.analysisFunction.setBasePath(this.outputBasePath);
            this.analysisFunction.setInputPath(this.outputBasePath);
            this.analysisFunction.setOutputPath(this.outputBasePath + "-analysis");
            this.analysisFunction.setFileSystem(FileSystem.newInstance(this.getConf()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public void setVisualizationFunction(Class<AbstractVisualizationFunction> function, String visualizationParams) {
        try {
            Class<AbstractVisualizationFunction> _tempClass = function;
            if (visualizationParams != null && visualizationParams.length() > 0) {
                Constructor<AbstractVisualizationFunction> ctor = _tempClass.getConstructor(String.class);
                this.visualizationFunction = (AbstractVisualizationFunction) ctor.newInstance(visualizationParams);
            } else {
                Constructor<AbstractVisualizationFunction> ctor = _tempClass.getConstructor();
                this.visualizationFunction = ctor.newInstance();
            }
            this.visualizationFunction.setBasePath(this.outputBasePath);
            this.visualizationFunction.setInputPath(this.analysisFunction.getResultPath());
            this.visualizationFunction.setOutputPath(this.outputBasePath + "-visualization");
            this.visualizationFunction.setFileSystem(FileSystem.newInstance(this.getConf()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int execute() {

        // Definizione output
        try {
            // Delete existing output
            FileSystem.get(this.conf).delete(new Path(outputBasePath), true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CrawlerFunction crawler : crawlers) {
            if (crawler != null) {
                crawler.collect();
            }
        }

        int exitCode = 0;
        try {
            exitCode = ToolRunner.run(this.getConf(), this, new String[0]);
            if (exitCode == 0) {
                if (analysisFunction != null) {
                    analysisFunction.run();
                }
                if (visualizationFunction != null) {
                    visualizationFunction.visualize();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitCode;
    }

}
