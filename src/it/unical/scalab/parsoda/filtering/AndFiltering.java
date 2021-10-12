package it.unical.scalab.parsoda.filtering;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.Pair;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class AndFiltering extends AbstractFilterFunction {
	
	Predicate<SocialItem> predicate = null;

	public AndFiltering() {
		super();
	}

	
	public AndFiltering(String options) {
		super(options);
		
		initFunctions();
		
	}

	private void initFunctions() {
		
		String[] filterFunctions = this.options.getString("f").split(",");
		String[] params = this.options.getString("p").split(",");
		List<Pair<String,String>> list = new LinkedList<Pair<String,String>>();
		
		for (int i=0; i< filterFunctions.length; i++) {
			if(params==null || params[i]==null) {
				list.add(new Pair<String, String>(filterFunctions[i], ""));	
			}else {
				list.add(new Pair<String, String>(filterFunctions[i], params[i]));
			}	
		}
				
		if (list.size() > 0) {
			predicate = list.stream().map(e -> createFilterFunction(e)).filter(Objects::nonNull)
					.reduce(e -> true, Predicate::and);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private Predicate<SocialItem> createFilterFunction(Pair<String,String> p) {
		try {
			
			Class<AbstractFilterFunction> _tempClass = (Class<AbstractFilterFunction>) Class.forName(p.getLeft().trim());
			AbstractFilterFunction function = null;
			if(p.getRight()!=null && p.getRight().trim().length()>0) {
				Constructor<AbstractFilterFunction> ctor = _tempClass.getConstructor(String.class);
				function = (AbstractFilterFunction) ctor.newInstance(p.getRight());
			}else {
				Constructor<AbstractFilterFunction> ctor = _tempClass.getConstructor();
				function = ctor.newInstance();
			}
			return (Predicate<SocialItem>) function;
		} catch (IllegalArgumentException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}	


	public boolean test(SocialItem g) {
		return this.predicate.test(g);
	}
	
	

}
