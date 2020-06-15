package com.resnik.util.serial.xml;

import com.resnik.util.serial.FileUtils;
import com.resnik.util.objects.structures.tree.SimpleTree;
import com.resnik.util.objects.structures.tree.TreeNode;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class XMLTree extends SimpleTree<XMLElement> {

    public static final String XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public XMLTree(){
    }

    public XMLTree(XMLNode root){
        super(root);
    }

    public XMLTree(Node element){
        this.add(new XMLElement(element));
    }

    @Override
    public TreeNode<XMLElement> generateNode(XMLElement value, TreeNode<XMLElement> parent) {
        return new XMLNode(value, parent);
    }

    public List<XMLNode> findAllByName(String name){
        if(this.root instanceof XMLNode){
            return ((XMLNode) this.root).findAllByName(name);
        }
        return Collections.emptyList();
    }

    public void save(String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        String extension = FileUtils.getFileExtension(file);
        if(!extension.equalsIgnoreCase("xml") && !extension.equalsIgnoreCase("txt")){
            throw new IllegalArgumentException("Must end in extension type: xml or txt");
        }
        PrintWriter pw = new PrintWriter(file);
        pw.println(XML_TAG);
        if(this.root != null){
            pw.println(this.root.toString());
        }
        pw.close();
    }

    public XMLNode getRoot(){
        return (XMLNode) super.getRoot();
    }



}
