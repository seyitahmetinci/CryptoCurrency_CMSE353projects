package MerkleTree;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import Node.Node;

import java.math.BigInteger; 
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 



public class MerkleTree {
	
	public static byte[] generateHash(String input) throws NoSuchAlgorithmException
    {   
		//SHA-256 Hashing Algorithm
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");       
        return md.digest(input.getBytes(StandardCharsets.UTF_8)); 
    }
    
	//Converts byte into hexadecimal value
    public static String toHexString(byte[] hash)
    {
        
        BigInteger number = new BigInteger(1, hash);         
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
         
        while (hexString.length() < 32) 
        { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
    }

    public static Node generateTree(ArrayList<String> dataBlocks) throws NoSuchAlgorithmException 
    {
        ArrayList<Node> childNodes = new ArrayList<>();

        for (String message : dataBlocks) {
            childNodes.add(new Node(null, null, toHexString(generateHash(message))));
        }

        return buildTree(childNodes);
    }

    private static Node buildTree(ArrayList<Node> children) throws NoSuchAlgorithmException 
    {
        ArrayList<Node> parents = new ArrayList<>();

        while (children.size() != 1) {
            int index = 0, length = children.size();
            while (index < length) {
                Node leftChild = children.get(index);
                Node rightChild = null;

                //add duplicate if the tree nodes are odd
                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                } else {
                    rightChild = new Node(null, null, leftChild.getHash());
                }

                String parentHash = toHexString(generateHash(leftChild.getHash() + rightChild.getHash()));
                parents.add(new Node(leftChild, rightChild, parentHash));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    //Print hash contents of tree starting from root to leaves
    private static void printTree(Node root) {
        if (root == null) {
            return;
        }

        if ((root.getLeft() == null && root.getRight() == null)) {
            System.out.println(root.getHash());
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                System.out.println(node.getHash());
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }
            
            if (node != null && node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node != null && node.getRight() != null) {
                queue.add(node.getRight());
            }

        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        ArrayList<String> dataBlocks = new ArrayList<>();
                
        Scanner sc= new Scanner(System.in);
        Scanner sc2= new Scanner(System.in);
        
        System.out.println("-------Welcome-------\n"
        					+"Press '1' to input\n"
        					+"Press '2' to build and display merkle tree\n"
        					+"Press '3' to exit program\n");
        boolean t=true;
        while (t){
        	System.out.println("Enter your choice:");
        	int ch = sc.nextInt();
        	
        	if (ch==1){
        		int i;
                System.out.println("Enter number of elements you want to enter");
                int num = sc.nextInt();
                
                for (i=0;i<num;i++) {
                		System.out.println("Enter element " + (i+1));
                		String s = sc2.nextLine();
                			
                		dataBlocks.add(s);   		 
                }
        	}
        	if (ch==2){
        		System.out.println("\nMerkle Hash Tree\n");	
                Node root = generateTree(dataBlocks);
                printTree(root);
        	}
        	
        	if (ch==3){
        		t=false;
        	}
        }                 	        	
    }
                          
}