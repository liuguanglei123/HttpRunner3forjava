package org.hrun.HrunArgsParse;

import com.beust.jcommander.IStringConverter;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class ArgsParseListConverter implements IStringConverter<List<String>> {
    @Override
    public List<String> convert(String args){
        List<String> result = Arrays.asList(args.split(","));
        return result;
    }
}
