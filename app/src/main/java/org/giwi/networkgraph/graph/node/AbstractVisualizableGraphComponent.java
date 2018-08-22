package org.giwi.networkgraph.graph.node;

import net.xqhs.graphs.representation.GraphRepresentation;
import net.xqhs.graphs.representation.RepresentationElement;
import net.xqhs.graphs.representation.VisualizableGraphComponent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AbstractVisualizableGraphComponent implements VisualizableGraphComponent
{
    protected Set<RepresentationElement> representations	= new HashSet<RepresentationElement>();

    @Override
    public void addRepresentation(RepresentationElement repr)
    {
        representations.add(repr);
    }

    @Override
    public Collection<RepresentationElement> getRepresentations()
    {
        return representations;
    }

    @Override
    public RepresentationElement getFirstRepresentationForRoot(GraphRepresentation root)
    {
        Collection<RepresentationElement> filtered = getRepresentationsForRoot(root);
        if(filtered.isEmpty())
            return null;
        return filtered.iterator().next();
    }

    @Override
    public Collection<RepresentationElement> getRepresentationsForRoot(GraphRepresentation root)
    {
        Collection<RepresentationElement> ret = new HashSet<RepresentationElement>();
        for(RepresentationElement repr : representations)
            if(repr.getParentRepresentation() == root)
                ret.add(repr);
        return ret;
    }
}