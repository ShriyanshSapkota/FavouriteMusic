/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FavouriteMusic;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

        



/**
 *
 * @author Shriyansh Sapkota
 */
public class mainFrame extends javax.swing.JFrame implements ActionListener {
    
    // animation timer related
    private Timer animationTimer;
    private int animationTimerInterval = 10; // in milliseconds
    
    
    // animation position related
    private BufferedImage ImgCd;
    private int startx = 100, starty = 200;
    private int xPos = 400;
    private int degrees =  0;

    
    // data/serialization related
    private ArrayList<Music> myFavouriteSongs;
    private String fileName = "MusicData.ser";
    int index = 0;

   
   
    
    // swing worker thread related
    SwingWorker worker;

    //MainFrame thread responsible for handling animation.
    public mainFrame() {
        initComponents();
        
        //An instance of the array list is saved in the variable.
        myFavouriteSongs = new ArrayList();
        
        
        //If serialized file doesnt exist, load and serialises the data.
        File f = new File(fileName); 
        if(f.isFile() == false && !f.isDirectory()) { 
            loadDataInnitial();
            serializeMusic();
        }   
        
        //Store an image in the variable called 'cd.png'
       try {
            ImgCd = ImageIO.read(new File("cd.png"));
        }   catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //Call the thread
        createAndRunLoadingThread();
        

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    
    public void actionPerformed(ActionEvent evt) {
        repaint();
    }
    
    //Responsible for the animation of the cds. Only draws it when the intropannel
    //is visible
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (IntroPannel.isVisible()) {
            drawCd(g);
            drawCd2(g);
        }        
        
          
    }
    
    
    /*
    These two methods use Java Graphics. First they rotate the image by incrementing 
    the degrees by two. Java graphics is used to draw the cd’s onto the screen. 
    One of the cd’s x position is incremented by two while the others is decremented, 
    this is how the cd moves across the screen. Furthermore, once the cd's reach the border,
    the cd's go back.
    */
    public void drawCd(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransformOp rotateOp = getRotateOp(degrees += 2);
        g2d.drawImage(ImgCd, rotateOp, startx += 1, starty);
        if (startx > 430) {
            g2d.drawImage(ImgCd, rotateOp, startx -= 2, starty);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    
    public void drawCd2(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransformOp rotateOp = getRotateOp(degrees += 2);
        g2d.drawImage(ImgCd, rotateOp, xPos -= 1, starty);
        if (xPos < 5) {
            g2d.drawImage(ImgCd, rotateOp, xPos += 2, starty);
        } 
        Toolkit.getDefaultToolkit().sync();
    }
    
    
    //Gets the centre of the image in order to rotate it.
    private AffineTransformOp getRotateOp(int degrees) {
        double radians = Math.toRadians(degrees);
        double xCentre = ImgCd.getWidth() / 2;
        double yCentre = ImgCd.getHeight() / 2;
        
        AffineTransform tx = AffineTransform.getRotateInstance(radians, xCentre, yCentre);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        
        return op;
      
    }
    
    // Writing data to the array list called my favourite songs. uses the music class and takes
    // in 3 strings and an integer
    private void loadDataInnitial() {
        Music m1 = new Music("Do Not Disturb", "Drake", "More Life", 2017);
        myFavouriteSongs.add(m1);
        Music m2 = new Music("Love Yourz", "J Cole", "2014 Forest Hills Drive", 2014);
        myFavouriteSongs.add(m2);
        Music m3 = new Music("Jungle", "A Boogie wit Da Hoodie", "Artist", 2016);
        myFavouriteSongs.add(m3);
        Music m4 = new Music("Every Season", "Roddy Ricch", "Feed Tha Streets 2", 2018);
        myFavouriteSongs.add(m4);
        Music m5 = new Music("Nights", "Frank Ocean", "Blond", 2016);
        myFavouriteSongs.add(m5);
        Music m6 = new Music("wokeuplikethis*", "Playboi Carti & Lil Uzi Vert", "Playboi Carti", 2017);
        myFavouriteSongs.add(m6);
        Music m7 = new Music("Poetic Justice", "Kendrick Lamar", "good kid,m.A.A.d city", 2012);
        myFavouriteSongs.add(m7);
        Music m8 = new Music("Drugs You Should Try it", "Travis Scott", "Days Before Rodeo", 2014);
        myFavouriteSongs.add(m8);
        Music m9 = new Music("Often", "The Weekend", "Beauty Behind The Madness", 2015);
        myFavouriteSongs.add(m9);
        Music m10 = new Music("Picture Me", "Dave", "Six Paths", 2016);
        myFavouriteSongs.add(m10);
        Music m11 = new Music("Signs","1Yush x AbizLoveLiz","Rookie Of The Year",2021);
        myFavouriteSongs.add(m11);
        Music m12 = new Music("PainKiller","AbizLoveLiz","Rookie Of The Year",2021);
        myFavouriteSongs.add(m12);
        Music m13 = new Music("Ready","1Yush","Rookie Of The Year",2021);
        myFavouriteSongs.add(m13);
    }
    
    
    private void createAndRunLoadingThread() {
        IntroPannel.setVisible(true);
        MainPannel.setVisible(false);
        animationTimer = new javax.swing.Timer(animationTimerInterval, this);
        animationTimer.start();
        
        worker = new SwingWorker<ArrayList<Music>, Void>() {
            @Override
            public ArrayList<Music> doInBackground() {
                final ArrayList<Music> dataLoading = deserializeMusic();
                deserializeMusic();
                try {
                    for (int c = 0; c <= 100;c++) {
                        Thread.sleep(100);
                        setProgress(c);
                        
        
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
                return dataLoading;
            }

            @Override
            public void done() {

                try {
                    // code separated out to make things easier
                    doThisWhenFinished();

                } catch (InterruptedException ignore) {
                } catch (java.util.concurrent.ExecutionException e) {
                    System.out.println("System error: concurrent exception: " + e.getMessage());
                }
            }

            private void doThisWhenFinished() throws InterruptedException, java.util.concurrent.ExecutionException {
                System.out.println("Task completed");
                IntroPannel.setVisible(false);
                animationTimer.stop();
                MainPannel.setVisible(true);
                myFavouriteSongs = get();
                DisplayData(0);
            }
                
        };
        
        worker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if ("progress".equals(evt.getPropertyName())) {
                jProgressBar1.setValue((Integer)evt.getNewValue());
                Progress_Label.setText(""+evt.getNewValue()+"%");
            }
        });
        
        worker.execute();
    }
    
    private void DisplayData(int i) {
        SongNameText.setText("");
        ArtistNameText.setText("");
        AlbumText.setText("");
        YearReleasedText.setText("");
        Music currentMusic = myFavouriteSongs.get(i);
        SongNameText.setText(currentMusic.getSongName());
        ArtistNameText.setText(currentMusic.getArtist());
        AlbumText.setText(currentMusic.getAlbum());
        YearReleasedText.setText(currentMusic.getYearReleased());
        index = i;
                    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        MainPannel = new javax.swing.JPanel();
        ExitButton = new javax.swing.JButton();
        SongNameText = new javax.swing.JTextField();
        ArtistNameText = new javax.swing.JTextField();
        AlbumText = new javax.swing.JTextField();
        YearReleasedText = new javax.swing.JTextField();
        SongName_Label = new javax.swing.JLabel();
        ArtistName_Label = new javax.swing.JLabel();
        Album_Label = new javax.swing.JLabel();
        YearReleased_Label = new javax.swing.JLabel();
        LastMusic = new javax.swing.JButton();
        NextMusic = new javax.swing.JButton();
        RandomMusic = new javax.swing.JButton();
        PrevMusic = new javax.swing.JButton();
        FirstMusic = new javax.swing.JButton();
        Title = new javax.swing.JLabel();
        IntroPannel = new javax.swing.JPanel();
        Progress_Label = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        Gif = new javax.swing.JLabel();
        Intro_Title = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLayeredPane1.setLayout(new java.awt.CardLayout());

        MainPannel.setBackground(new java.awt.Color(102, 102, 255));
        MainPannel.setMaximumSize(new java.awt.Dimension(600, 425));
        MainPannel.setName(""); // NOI18N
        MainPannel.setPreferredSize(new java.awt.Dimension(600, 425));

        ExitButton.setBackground(new java.awt.Color(255, 51, 51));
        ExitButton.setText("Exit");
        ExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitButtonActionPerformed(evt);
            }
        });

        SongNameText.setToolTipText("");
        SongNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SongNameTextActionPerformed(evt);
            }
        });

        AlbumText.setToolTipText("");

        SongName_Label.setText("Song Name");

        ArtistName_Label.setText("Artist Name");

        Album_Label.setText("Album");

        YearReleased_Label.setText("Year Released");

        LastMusic.setBackground(new java.awt.Color(51, 255, 51));
        LastMusic.setForeground(new java.awt.Color(0, 0, 0));
        LastMusic.setText("Last Song");
        LastMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastMusicActionPerformed(evt);
            }
        });

        NextMusic.setBackground(new java.awt.Color(102, 255, 0));
        NextMusic.setForeground(new java.awt.Color(0, 0, 0));
        NextMusic.setText("Next Song");
        NextMusic.setToolTipText("");
        NextMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextMusicActionPerformed(evt);
            }
        });

        RandomMusic.setBackground(new java.awt.Color(102, 255, 51));
        RandomMusic.setForeground(new java.awt.Color(0, 0, 0));
        RandomMusic.setText("Random");
        RandomMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RandomMusicActionPerformed(evt);
            }
        });

        PrevMusic.setBackground(new java.awt.Color(51, 255, 51));
        PrevMusic.setForeground(new java.awt.Color(0, 0, 0));
        PrevMusic.setText("Prev Song");
        PrevMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrevMusicActionPerformed(evt);
            }
        });

        FirstMusic.setBackground(new java.awt.Color(0, 255, 0));
        FirstMusic.setForeground(new java.awt.Color(0, 0, 0));
        FirstMusic.setText("First Song");
        FirstMusic.setToolTipText("");
        FirstMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstMusicActionPerformed(evt);
            }
        });

        Title.setFont(new java.awt.Font("Dialog", 3, 36)); // NOI18N
        Title.setForeground(new java.awt.Color(255, 255, 255));
        Title.setText("My Favourite Songs!");

        javax.swing.GroupLayout MainPannelLayout = new javax.swing.GroupLayout(MainPannel);
        MainPannel.setLayout(MainPannelLayout);
        MainPannelLayout.setHorizontalGroup(
            MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPannelLayout.createSequentialGroup()
                .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPannelLayout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(ExitButton))
                    .addGroup(MainPannelLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ArtistName_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Album_Label)
                            .addComponent(YearReleased_Label)
                            .addComponent(SongName_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SongNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AlbumText, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ArtistNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(YearReleasedText, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(MainPannelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(FirstMusic)
                .addGap(18, 18, 18)
                .addComponent(PrevMusic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RandomMusic)
                .addGap(28, 28, 28)
                .addComponent(NextMusic)
                .addGap(29, 29, 29)
                .addComponent(LastMusic)
                .addGap(44, 44, 44))
        );
        MainPannelLayout.setVerticalGroup(
            MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPannelLayout.createSequentialGroup()
                .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPannelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ExitButton))
                    .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(MainPannelLayout.createSequentialGroup()
                        .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SongNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SongName_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ArtistNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ArtistName_Label))
                        .addGap(68, 68, 68))
                    .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Album_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AlbumText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(YearReleased_Label)
                    .addComponent(YearReleasedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(MainPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LastMusic)
                    .addComponent(NextMusic)
                    .addComponent(RandomMusic)
                    .addComponent(FirstMusic)
                    .addComponent(PrevMusic))
                .addGap(45, 45, 45))
        );

        jLayeredPane1.add(MainPannel, "card2");

        IntroPannel.setBackground(new java.awt.Color(204, 204, 0));
        IntroPannel.setMaximumSize(new java.awt.Dimension(600, 425));

        Progress_Label.setFont(new java.awt.Font("Dialog", 1, 21)); // NOI18N
        Progress_Label.setForeground(new java.awt.Color(0, 0, 0));
        Progress_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jProgressBar1.setBackground(new java.awt.Color(0, 51, 255));
        jProgressBar1.setForeground(new java.awt.Color(0, 204, 255));

        Gif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Gif.setIcon(new javax.swing.ImageIcon("/Users/shriyansh/Desktop/Uni/3rd Term (Jan-Jun 2021)/Software Engineering/assignmentBuild 3/src/main/java/Images/unnamed.gif")); // NOI18N

        Intro_Title.setFont(new java.awt.Font("Dialog", 3, 24)); // NOI18N
        Intro_Title.setForeground(new java.awt.Color(255, 51, 51));
        Intro_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Intro_Title.setText("My Favourite Songs!");

        javax.swing.GroupLayout IntroPannelLayout = new javax.swing.GroupLayout(IntroPannel);
        IntroPannel.setLayout(IntroPannelLayout);
        IntroPannelLayout.setHorizontalGroup(
            IntroPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IntroPannelLayout.createSequentialGroup()
                .addGroup(IntroPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IntroPannelLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(IntroPannelLayout.createSequentialGroup()
                        .addGap(261, 261, 261)
                        .addComponent(Progress_Label)))
                .addContainerGap(137, Short.MAX_VALUE))
            .addComponent(Gif, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Intro_Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        IntroPannelLayout.setVerticalGroup(
            IntroPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IntroPannelLayout.createSequentialGroup()
                .addComponent(Intro_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Gif, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(Progress_Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        jLayeredPane1.add(IntroPannel, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FirstMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstMusicActionPerformed
        DisplayData(0);
    }//GEN-LAST:event_FirstMusicActionPerformed

    private void PrevMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrevMusicActionPerformed
        int min = 0;
        int max = myFavouriteSongs.size() - 1;
        int updatedIndex = index-1;
        if (updatedIndex < min) {
                updatedIndex = max;
        }
        DisplayData(updatedIndex);
    }//GEN-LAST:event_PrevMusicActionPerformed

    private void ExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitButtonActionPerformed
        loadDataInnitial();
        serializeMusic();
        System.exit(0);
    }//GEN-LAST:event_ExitButtonActionPerformed

    private void NextMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextMusicActionPerformed
        int max = myFavouriteSongs.size() - 1;
        int updatedIndex = index + 1;
        if (updatedIndex > max) {
            updatedIndex = 0;
        }
        DisplayData(updatedIndex);
    }//GEN-LAST:event_NextMusicActionPerformed

    private void LastMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastMusicActionPerformed
        int max = myFavouriteSongs.size() - 1;
        DisplayData(max);
                
    }//GEN-LAST:event_LastMusicActionPerformed

    private void RandomMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RandomMusicActionPerformed
        Random rand = new Random();
        int randomIndex = rand.nextInt(myFavouriteSongs.size());
        DisplayData(randomIndex);
        
    }//GEN-LAST:event_RandomMusicActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            
            animationTimer.stop();
            loadDataInnitial();
            serializeMusic();
            JOptionPane.showMessageDialog(this, "You have chosen to exit the program!");
            System.exit(0);
        }   
    }//GEN-LAST:event_formKeyPressed

    private void SongNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SongNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SongNameTextActionPerformed

    public void serializeMusic() {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(myFavouriteSongs);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Music> deserializeMusic() {
        ArrayList<Music> songs = new ArrayList();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            songs = (ArrayList<Music>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            
        } catch (ClassNotFoundException c) {
            System.out.println("Music class not found");
            c.printStackTrace();
            
        }
        return songs;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AlbumText;
    private javax.swing.JLabel Album_Label;
    private javax.swing.JTextField ArtistNameText;
    private javax.swing.JLabel ArtistName_Label;
    private javax.swing.JButton ExitButton;
    private javax.swing.JButton FirstMusic;
    private javax.swing.JLabel Gif;
    private javax.swing.JPanel IntroPannel;
    private javax.swing.JLabel Intro_Title;
    private javax.swing.JButton LastMusic;
    private javax.swing.JPanel MainPannel;
    private javax.swing.JButton NextMusic;
    private javax.swing.JButton PrevMusic;
    public static javax.swing.JLabel Progress_Label;
    private javax.swing.JButton RandomMusic;
    private javax.swing.JTextField SongNameText;
    private javax.swing.JLabel SongName_Label;
    private javax.swing.JLabel Title;
    private javax.swing.JTextField YearReleasedText;
    private javax.swing.JLabel YearReleased_Label;
    private javax.swing.JLayeredPane jLayeredPane1;
    public static javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
