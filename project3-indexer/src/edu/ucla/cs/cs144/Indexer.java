package edu.ucla.cs.cs144;

import edu.ucla.cs.cs144.Entry;
import java.util.HashMap;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    public Indexer() {}

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }

   public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }

   public void indexEntry(Entry entry) throws IOException {

        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("itemID", entry.getItemID(), Field.Store.YES));
        doc.add(new StringField("name", entry.getName(), Field.Store.YES));
        doc.add(new TextField("category", entry.getCategory(), Field.Store.YES));
        doc.add(new TextField("content", entry.toContent(), Field.Store.NO));
        writer.addDocument(doc);
    } 
 
    public void rebuildIndexes() throws IOException{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
        Statement s = conn.createStatement();
        //create a hashMap that stores Entry
        HashMap<Integer, Entry> map = new HashMap<Integer, Entry>();

        //fill itemID and category field
        ResultSet rs = s.executeQuery("SELECT ItemID, Category FROM item_cat");
        while( rs.next() ){
            Integer itemID = new Integer(rs.getInt("ItemID"));
            String category = rs.getString("Category");
            if(!map.containsKey(itemID)){
                map.put(itemID, new Entry());
            }
            ((Entry)map.get(itemID)).setItemID(itemID.toString());
            ((Entry)map.get(itemID)).appendCategory(category);
        }

        //fill name and description field
        rs = s.executeQuery("SELECT ItemID, Name, Description FROM item");
        while( rs.next() ){
            int itemID = rs.getInt("ItemID");
            String name = rs.getString("Name");
            String description = rs.getString("description");
            if(!map.containsKey(itemID)){
                map.put(itemID, new Entry());
            }
            ((Entry)map.get(itemID)).setName(name);
            ((Entry)map.get(itemID)).setDescription(description);
        }

        System.out.println("mapSize="+map.size());

        //index each Entry
        getIndexWriter(true);
        System.out.println("start indexing Entry");
        for(Entry e : map.values()){
            indexEntry(e);
        }
        closeIndexWriter();

	} catch (SQLException ex) {
	    System.out.println(ex);
	}

        // close the database connection
	try {
	    conn.close();
	} 
    catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) throws IOException{
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        System.out.println("done!");
    }   
}
