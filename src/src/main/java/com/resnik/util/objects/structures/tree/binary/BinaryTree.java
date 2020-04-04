package com.resnik.util.objects.structures.tree.binary;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryTree<T> implements Collection<T> {

    public static final String TAG = BinaryTree.class.getSimpleName();

    private BinaryNode<T> root;

    public BinaryTree() {
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        return root.size();
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty()) {
            return false;
        }
        return root.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.toListValues().iterator();
    }

    @Override
    public Object[] toArray() {
        if (this.root == null) {
            return null;
        }
        List<T> retList = (List<T>) root.getListRepSorted();
        return retList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        if (this.root == null || ts == null) {
            return null;
        }
        List<T> retList = (List<T>) root.getListRepSorted();
        ts = retList.toArray(ts);
        return ts;
    }

    public List<T> toListValues(){
        if (this.root == null) {
            return null;
        }
        List<T> retList = root.getListRepSorted();
        return retList;
    }
    
    public List<BinaryNode<T>> toListNodes(){
        if(this.root == null){
            return null;
        }
        return root.getListNodeSorted();
    }
    
    @Override
    public boolean add(T e) {
        if (root == null) {
            root = new BinaryNode(e);
            return true;
        }
        return root.insert(e);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof BinaryNode) {
            return removeNode((BinaryNode) o);
        }
        try {
            T inputVal = (T) o;
            return removeValue(inputVal);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeNode(BinaryNode<T> inputNode) {
        if (inputNode == null) {
            return false;
        }
        return removeValue(inputNode.value);
    }

    public boolean removeValue(T value) {
        if (value == null) {
            return false;
        }
        BinaryNode<T> nodeRep = root.getNode(value);
        if (nodeRep == null) {
            return false;
        }
        if (nodeRep.parent == null) {
            this.clear();
        }
        if (nodeRep.parent.left == nodeRep) {
            nodeRep.parent.left = null;
            boolean ret = true;
            ret &= nodeRep.parent.insert(nodeRep.left);
            ret &= nodeRep.parent.insert(nodeRep.right);
            return ret;
        }
        if (nodeRep.parent.right == nodeRep) {
            nodeRep.parent.right = null;
            boolean ret = true;
            ret &= nodeRep.parent.insert(nodeRep.left);
            ret &= nodeRep.parent.insert(nodeRep.right);
            return ret;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        boolean retVal = true;
        for (T currVal : clctn) {
            retVal &= this.add(currVal);
        }
        return retVal;
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        boolean retVal = true;
        for (Object currVal : clctn) {
            try {
                T extended = (T) currVal;
                retVal &= this.remove(extended);
            } catch (Exception e) {
                retVal &= false;
            }
        }
        return retVal;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        this.root = null;
    }

    public String childString(T value) {
        if (this.root == null || value == null) {
            return null;
        }
        BinaryNode<T> refNode = root.getNode(value);
        if (refNode == null) {
            return null;
        }
        return refNode.childString();
    }

    public List<BinaryNode<T>> getAllNodes(T inputValue){
        if(this.root == null || inputValue == null){
            return null;
        }
        return this.root.getAllNodes(inputValue);
    }
    
    public List<String> childStringList(){
        List<String> retList = new ArrayList();
        List<BinaryNode<T>> allNodes = this.toListNodes();
        for(BinaryNode<T> currNode : allNodes){
            retList.add(currNode.childString());
        }
        return retList;
    }
    
    public int getHeight(){
        if(root == null){
            return 0;
        }
        return root.getHeight();
    }
    
    public int getHeightOfNode(BinaryNode<T> inputNode){
        if(inputNode == root){
            return 0;
        }
        if(this.root.containsNode(inputNode)){
            return getHeightOfNode(inputNode.parent) + 1;
        }
        return -1;
    }
    
    public int getWidthOfNode(BinaryNode<T> inputNode){
        if(inputNode == root){
            return 0;
        }
        if(inputNode == null){
            return -1;
        }
        List<BinaryNode<T>> tempList = this.toListNodes();
        for (int i = 0; i < tempList.size(); i++) {
            if(Objects.equals(tempList.get(i).value, inputNode.value)){
                return i;
            }
        }
        return -1;
    }
    
    public byte[][][] getImage(BinaryNode<T> inputNode, int size){
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, size);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth("" + inputNode.value);
        int height = fm.getHeight();
        g2d.dispose();
        
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        Color back_color_awt = Color.WHITE;
        byte[] back_color = ImageUtils.awtToByte(back_color_awt);
//        g2d.setBackground(back_color_awt);
        AttributedString as1 = new AttributedString("" + inputNode.value);
        as1.addAttribute(TextAttribute.BACKGROUND, back_color_awt);
        as1.addAttribute(TextAttribute.FONT, fm);
        as1.addAttribute(TextAttribute.SIZE, size);
        as1.addAttribute(TextAttribute.BACKGROUND, back_color_awt);
        g2d.drawString(as1.getIterator(), 0, fm.getAscent());
        g2d.dispose();

        byte[][][] tempImage = ImageUtils.bufferedImageToBytes(img, BufferedImage.TYPE_INT_RGB);
        int padding = size / 3;
        byte[][][] retImage = new byte[tempImage.length][tempImage[0].length + 2*padding][];
        for (int ROW = 0; ROW < retImage.length; ROW++) {
            for (int COL = 0; COL < retImage[0].length; COL++) {
                retImage[ROW][COL] = back_color;
            }
        }
        for (int ROW = 0; ROW < tempImage.length; ROW++) {
            for (int COL = 0; COL < tempImage[0].length; COL++) {
                retImage[ROW][COL + padding] = tempImage[ROW][COL];
            }
        }
        retImage = ImageUtils.addBorder(retImage);
        
        return retImage;
    }
    
    public byte[][][] writeNodes(byte[] background){
        List<BinaryNode<T>> nodeList = this.root.getListNodeSorted();
        int size = 30;
        new File("nodes/binary/").mkdirs();
        Map<T, byte[][][]> imageMap = new LinkedHashMap();
        int a = size;
        for(BinaryNode<T> currNode : nodeList){
            byte[][][] currImage = this.getImage(currNode, size);
            a = currImage.length;
            imageMap.put(currNode.value, currImage);
        }
        Map<T, Integer> rowIndexMap = new LinkedHashMap();
        Map<T, Integer> colIndexMap = new LinkedHashMap();
        
        int padding = 30;
        int totalWidth = padding;
        int totalHeight = padding;
        for(BinaryNode<T> currNode : nodeList){
            colIndexMap.put(currNode.value, totalWidth);
            totalWidth += imageMap.get(currNode.value)[0].length + padding;
            int row = this.getHeightOfNode(currNode);
            int rowIndex = row*a +(row  + 1)*padding;
            rowIndexMap.put(currNode.value, rowIndex);
            totalHeight = Math.max(totalHeight, rowIndex + a +  padding);
        }
        
        Log.v(TAG,colIndexMap);
        Log.v(TAG,"totalWidth:" + totalWidth);
        Log.v(TAG,rowIndexMap);
        Log.v(TAG,"totalHeight:" + totalHeight);
        byte[][][] writeImage = new byte[totalHeight][totalWidth][];
        for(int ROW = 0; ROW < totalHeight; ROW++){
            for (int COL = 0; COL < totalWidth; COL++) {
                writeImage[ROW][COL] = background;
            }
        }
        // Draw all lines
        for(BinaryNode<T> currNode : nodeList){
            byte[][][] currImage = imageMap.get(currNode.value);
            int currHeight = currImage.length;
            int currWidth = currImage[0].length;
            int currColIndex = colIndexMap.get(currNode.value) + currWidth/2;
            int currRowIndex = rowIndexMap.get(currNode.value) + currHeight/2;
            if(currNode.left != null){
                // left middle to curr middle
                byte[][][] leftImage = imageMap.get(currNode.left.value);
                int leftWidth = leftImage[0].length;
                int leftHeight = leftImage.length;
                int leftColIndex = colIndexMap.get(currNode.left.value) + leftWidth / 2;
                int leftRowIndex = rowIndexMap.get(currNode.left.value) + leftHeight / 2;
                double dRow = (leftRowIndex - currRowIndex);
                double dCol = (leftColIndex - currColIndex);
                double m = dRow / dCol;
                for (int COL = leftColIndex; COL < currColIndex; COL++) {
                    for (int i = 0; i < 4; i++) {
                    int ROW = (int) (m*(COL - leftColIndex) + leftRowIndex + i);
                    writeImage[ROW][COL] = ImageUtils.BLACK_B;
                    }
                }
                
            }
            if(currNode.right != null){
                byte[][][] rightImage = imageMap.get(currNode.right.value);
                int rightWidth = rightImage[0].length;
                int rightHeight = rightImage.length;
                int rightColIndex = colIndexMap.get(currNode.right.value) + rightWidth / 2;
                int rightRowIndex = rowIndexMap.get(currNode.right.value) + rightHeight / 2;
                double dRow = (rightRowIndex - currRowIndex);
                double dCol = (rightColIndex - currColIndex);
                double m = dRow / dCol;
                for (int COL = currColIndex; COL < rightColIndex; COL++) {
                    for (int i = 0; i < 4; i++) {
                    int ROW = (int) (m*(COL - rightColIndex) + rightRowIndex + i);
                    writeImage[ROW][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
        }
        
        // Draw all nodes
        for(BinaryNode<T> currNode : nodeList){
            int colStartIndex = colIndexMap.get(currNode.value);
            int rowStartIndex = rowIndexMap.get(currNode.value);
            byte[][][] currImage = imageMap.get(currNode.value);
            for (int ROW = 0; ROW < currImage.length; ROW++) {
                for (int COL = 0; COL < currImage[0].length; COL++) {
                    writeImage[ROW + rowStartIndex][COL + colStartIndex] = currImage[ROW][COL]; 
                }
            }
        }
        return writeImage;
    }
    
    public void writeImage(){
        if(root == null){
            return;
        }
        int a = 30;
        int h = this.getHeight();
        int H = h*a + (h -1) * a + 2*a;
        int W = (int) (Math.pow(2.0, h)*a + (Math.pow(2.0, h) - 1)*2*a + 2*a);
        byte[][][] retImage = new byte[H][W][];
    }
    
    public static BufferedImage[] getImages(List<Double> allValues){
        BinaryTree<Double> b = new BinaryTree();
        List<byte[][][]> allImages = new ArrayList();
        int maxWidth = 0;
        int maxHeight = 0;
        byte[] background = ImageUtils.GREY_B(0.5);
        for (int i = 0; i < allValues.size(); i++) {
            b.add(allValues.get(i));
            byte[][][] curr = b.writeNodes(background);
            maxHeight = Math.max(maxHeight, curr.length);
            maxWidth = Math.max(maxWidth, curr[0].length);
            allImages.add(curr);
        }
        int padding = 100;
        List<BufferedImage> writeImages = new ArrayList();
        int count = 0;
        for(byte[][][] currImage : allImages){
            byte[][][] tempImage = new byte[maxHeight + 2*padding][maxWidth][];
            for (int ROW = 0; ROW < tempImage.length; ROW++) {
                for (int COL = 0; COL < tempImage[0].length; COL++) {
                    tempImage[ROW][COL] = background;
                }
            }
            int dWidth = (maxWidth - currImage[0].length)/2;
            for (int ROW = 0; ROW < currImage.length; ROW++) {
                for (int COL = 0; COL < currImage[0].length; COL++) {
                    tempImage[ROW + padding][COL + dWidth] = currImage[ROW][COL];
                }
            }
            writeImages.add(ImageUtils.bytesToBufferedImage(tempImage));
            count++;
        }
        BufferedImage[] retImages = writeImages.toArray(new BufferedImage[writeImages.size()]);
        return retImages;
    }
    
    public static void generateBinaryGif(String outDir, List<Double> allValues ){
        BufferedImage[] binaryGif = getImages(allValues);
        GifDecoder gd = new GifDecoder();
        try {
            ImageUtils.saveGifBuffered(binaryGif, gd, outDir, 500, true);
        } catch (IOException ex) {
            Logger.getLogger(BinaryTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
