// This code is released under AGPL License. For more information read the LICENSE file.
// Licensor: Fahimeh Bayeh
// NOTE: The code is incomplete. Please contact me for the complete code.

public class Othello extends JFrame 
{
		  // class of each cells in the board
		  static class Cell extends JPanel
		  {
			  int x_index; 						// x-index of the cell in the board
			  int y_index; 						// y-index of the cell in the board
			  int state;						// State of the cells which shows whether the cell is a) blank b) taken by player 1 c) taken by player 2
			  private Image backgroundImage;	// Background image of cell which shows its state
			  
			  public Cell ()
			  {
			  }
			  
			  public Cell (BorderLayout bl)
			  {
				super(bl);  
			  }
			  
			  public void paintComponent(Graphics g) {
				    super.paintComponent(g);

				    // Draw the background image.
				    g.drawImage(backgroundImage, 0, 0, this);
			 }
			  
		  }
		  
		  static class MouseAdapterMod extends MouseAdapter {

			   // It takes the cell that the player clicked
			   public void mouseClicked(MouseEvent e) {
			       Cell cell = (Cell)e.getSource();
			       playerMove[0] = cell.x_index;
			       playerMove[1] = cell.y_index;
			       playerTurn = false;                 // The player has made its move
			   }
			}
		  
		  // The board weight which AI algorithm use for decision making
		  private static final int[][] boardWeight = {{70,  1, 35, 30, 30, 35,  1, 70},
			   										  { 1,  1, 25, 25, 25, 25,  1,  1},
			   										  {35, 25, 30, 26, 26, 30, 25, 35},
			   										  {30, 25, 26, 25, 25, 26, 25, 30},
			   										  {30, 25, 26, 25, 25, 26, 25, 30},
			   										  {35, 25, 30, 26, 26, 30, 25, 35},
			   										  { 1,  1, 25, 25, 25, 25,  1,  1},
			   										  {70,  1, 35, 30, 30, 35,  1, 70}};
		  
		  // keeps the current state of game
	      public static Cell[][] cells = new Cell[8][8];
	      
	      public static int[] playerMove =  new int[2];
	      
	      public static Boolean playerTurn = true;                    // Shows whether it is the players turn or the system
	      
	      public static MouseAdapterMod mam = new MouseAdapterMod();
	      
	      public static int[][] possibleCells = new int[20][2];

	      private JPanel pnlMain = new JPanel(new GridLayout(8,8));

	      private Container c;
		  
	      public static void main(String[] args)

	      {
	          final Othello app = new Othello();             
	            
	      }

	      // the whole constructor is for setting up the UI of the board

	      public Othello()

	      {
	    	  int[] playersCount = new int[2];				// Keeps the number of cells each player has taken
	    	  double secondPlayerWins = 0, playersEqual=0;
	    	  boolean[] IsGameInProgress = {true,true};		// Shows whether game is progressing without problem for both players
	    	  int turnOfGame = 0;							// For passing the game between two players
	    	  
	    	  
	    	  //initializing the cells
	    	  for(int id=0;id<8;id++)
	    	  {
	    		  for(int j=0;j<8;j++)
	    		  {
	    			  cells[id][j] = new Cell();
	    			  cells[id][j].x_index = id;
	    			  cells[id][j].y_index = j;
	    			  if((id == 3 && j == 3) || (id == 4 && j == 4))
	    			  {
	    				  cells[id][j].state = 1;
	    			  }
	    			  else if((id == 3 && j == 4) || (id == 4 && j == 3))
	    			  {
	    				  cells[id][j].state = 2;
	    			  }
	    			  else
	    			  {
	    				  cells[id][j].state = 0;
	    			  }
	    		  }
	    	  }
	    	  
	    	  //Setting up the board
	          c = getContentPane();
	          setBounds(100, 100, 470, 495);
	          setBackground(new Color(1, 95, 14));
	          setDefaultCloseOperation(EXIT_ON_CLOSE);
	          setTitle("Othello");
	          setResizable(false);
	          c.setLayout(null);
	          pnlMain.setBounds(3, 3, 460, 460);
	          pnlMain.setBackground(new Color(1, 95, 14));
	          c.add(pnlMain);
	          this.drawBoard();
	            
	          setVisible(true);

	          while(IsGameInProgress[0] || IsGameInProgress[1])
		      {
	        	  // For the animations 
	        	  try {
	        		  Thread.sleep(1000);                 //1000 milliseconds is one second.
	        	  } catch(InterruptedException ex) {
	        		  Thread.currentThread().interrupt();
	        	  }
	        	  
	        	  // The player one is the system and it plays random strategy
	        	  if(turnOfGame%2 == 0)
	        	  {
	        		  
	        		  int n = PossibleMoves(cells,1);
	    	    	  
	    	    	  if(n != 0)
	    	    	  {
		        		  IsGameInProgress[0] = RandomStrategy(1);
	    	    	  }
	    	    	  else
	    	    	  {
	    	    		  if(IsGameFinished(cells))
	    	    		  {
	    	    			  break;
	    	    		  }
	    	    	  }
	        		  turnOfGame++;
	        	  }
	        	  else if(turnOfGame%2 == 1 && !playerTurn)
	        	  {
	        		  int n = PossibleMoves(cells,2);
	    	    	  
	    	    	  if(n != 0)
	    	    	  {
	    	    		  playerTurn = true;
	    	    		  IsGameInProgress[0] = IsMoveValid(cells, playerMove[0], playerMove[1], 2);
	    	    		 
	    	    		  if(IsGameInProgress[0])
	    	    		  {
	    	    			  ApplyChange(playerMove[0], playerMove[1], 2);
	    	    			  turnOfGame++;
	    	    		  }
	    	    	  }
	    	    	  else
	    	    	  {
	    	    		  // If the player doesn't have any move, it checks whether systems has any move or not, if neither have any move it stops the game
	    	    		  if(IsGameFinished(cells))
	    	    		  {
	    	    			  break;
	    	    		  }
	    	    		  else
	    	    		  {
	    	    			  playerTurn = true;
		    	    		  turnOfGame++;
	    	    		  }
	    	    	  }
	        	
	        	  }
	        	  
		      }

	          
	          playersCount = BoardCount(cells);
	          
	          System.out.println("Game over");
	          if(playersCount[0]<playersCount[1])
	          {
	        	  System.out.println("Second Player Wins");
	          }
	          else if(playersCount[0] == playersCount[1])
	          {
	        	  System.out.println("It is a draw");
	          }
	          else
	          {
	        	  System.out.println("First Player Wins");
	          }
	      }
	      
	 


	      // This method draws board
	      private void drawBoard()
	      {
	    	
	    	  pnlMain.removeAll();
	          for (int y = 0; y < 8; y++)
	          {
	        	  for (int x = 0; x < 8; x++)
	        	  {
	        		  cells[y][x].setLayout(new BorderLayout());
	        		  cells[y][x].setBorder(BorderFactory.createBevelBorder(0,new Color(0,0,0),new Color(3,128,20)));
	        		  cells[y][x].setBackground(new Color(1, 95, 14));
	        		  cells[y][x].addMouseListener(mam);
	        		  cells[y][x].add(this.getImage(cells[y][x],false), BorderLayout.CENTER);
	        		  cells[y][x].validate();
	        		  pnlMain.add(cells[y][x]);
	              }
	          }
	      }

	      //Random strategy: Choose a random cell from the possible moves
	      private boolean RandomStrategy(int turn)
	      {
	    	  Random r = new Random();
	    	  int n = PossibleMoves(cells,turn);
	    	  
	    	  if(n != 0)
	    	  {
	    		  int index = r.nextInt(n);
	    			  
	    		  ApplyChange(possibleCells[index][0],possibleCells[index][1],turn);
	    		  return true;
	    	  }
	    	  return false;
	      }
	      
	      // Greedy strategy: Choose the cell which gives the maximum cells (it only consider the immediate result)
	      private boolean GreedyStrategy(int turn)
	      {
	    	  int n = PossibleMoves(cells,turn);
	    	  int[] playersCount = new int[2];
	    	  int[] optimum = new int[2];
	    	  
	    	  optimum[0] = -1; optimum[1] = -1;
	    	  
	    	  for(int i=0; i < n ;i++)
	    	  {
		    	  Cell[][] temp = new Cell[8][8]; 
		    	  n = PossibleMoves(cells,turn);
		    	  CopyArray(cells,temp);
		    		
	    		  TempApplyChange(temp,possibleCells[i][0],possibleCells[i][1],turn);
	    		  if(n != 0)
	    		  {
	    			  playersCount = BoardCount(temp);
    			  
	    			  if(turn == 1)
	    			  {
    					  if(playersCount[0] > optimum[1])
    					  {
    						  optimum[0] = i;
    						  optimum[1] = playersCount[0];
    					  }
	    			  }
	    			  else if(turn == 2)
	    			  {
	    				  if(playersCount[1] > optimum[1])
	    				  {
	    					  optimum[0] = i;
	    					  optimum[1] = playersCount[1];
	    				  }
	    			  }
	    		  }
	    	  }
	    	  if(optimum[1] != -1 &&  optimum[0] != -1)
	    	  {
	    		  ApplyChange(possibleCells[optimum[0]][0],possibleCells[optimum[0]][1],turn);
	    		  return true;
	    	  }
	    	  return false;
	      }

	      
	      // For copying a two dimensional array (used to make a copy of the current state of game for further calculation)
	      private static void CopyArray(Cell[][] src,Cell[][] target) 
	      {
	    	   for (int i = 0; i < 8; i++) 
	    	   {
	    		   for (int j = 0; j < 8; j++)
	    		   {
	    			   target[i][j] = CopyCell(src[i][j]);
	    		   }
	    	   }
	      }
	      
	      
	      // Runs the minimax function and if the move is valid, it applies it to the board
	      private boolean MinimaxStrategy(int turn)
	      {
	    	  Cell[][] temp = cells;
	    	  int[] move = new int[2];
	    	  
	    	  move = NormalMinimax(temp,2,turn,true);
	    	  
	    	  int n = PossibleMoves(cells,turn);
	    	  if(move[0] != -1 && move[0] < n)
	    	  {
	    		  
	    		  ApplyChange(possibleCells[move[0]][0],possibleCells[move[0]][1],turn);
	    		  return true;
	    	  }
	    	  
	    	  return false;
	      }
	      
	      // The recursive minimax function. It gives the best choice based on minimax algorithm
	      private int[] NormalMinimax(Cell[][] gameCells,int depth,int turn,boolean isMax)
	      {
	    	  int n = PossibleMoves(gameCells,turn); 
	    	  int[] playersCount = new int[2];
	    	  int[] optimum = new int[2], minimaxResult = new int[2];
	    	  
	    	  optimum[0] = -1; optimum[1] = -1;
	    	  minimaxResult[0] = 0; minimaxResult[1] = 0;
	    	  
	    	  for(int i=0; i < n ;i++)
	    	  {
		    	  Cell[][] temp = new Cell[8][8]; 
		    	  n = PossibleMoves(gameCells,turn);
		    	  CopyArray(gameCells,temp);
		    		
	    		  TempApplyChange(temp,possibleCells[i][0],possibleCells[i][1],turn);

	    		  if(depth == 0)
	    		  {
	    			  playersCount = BoardCount(temp);
	    			  
	    			  if(turn == 1)
		    		  {
		    			  if(isMax)
		    			  {
		    				  if(playersCount[0] > optimum[1])
		    				  {
		    					  optimum[0] = i;
		    					  optimum[1] = playersCount[0];
		    				  }
		    			  }
		    			  else
		    			  {
		    				  if(optimum[1] == -1)
		    				  {
		    					  optimum[0] = i;
		    					  optimum[1] = playersCount[0];
		    				  }
		    				  else
		    				  {
		    					  if(playersCount[0] < optimum[1])
		    					  {
			    					  optimum[0] = i;
		    						  optimum[1] = playersCount[0];
		    					  }
		    				  }
		    			  }
		    		  }
		    		  else if(turn == 2)
		    		  {
		    			  if(isMax)
		    			  {
		    				  if(playersCount[1] > optimum[1])
		    				  {
		    					  optimum[0] = i;
		    					  optimum[1] = playersCount[1];
		    				  }
		    			  }
		    			  else
		    			  {
		    				  if(optimum[1] == -1)
		    				  {
		    					  optimum[0] = i;
		    					  optimum[1] = playersCount[1];
		    				  }
		    				  else
		    				  {
		    					  if(playersCount[1] < optimum[1])
		    					  {
			    					  optimum[0] = i;
		    						  optimum[1] = playersCount[1];
		    					  }
		    				  }
		    			  }
		    		  }
	    		  }
	    		  else
	    		  {
	    			  minimaxResult = NormalMinimax(temp,depth-1,turn,!isMax);
	    			  
	    	    	  n = PossibleMoves(gameCells,turn);
	    			  
	    			  if(minimaxResult[1] == -1)
	    			  {
	    				  playersCount = BoardCount(temp);
		    			  
		    			  if(turn == 1)
			    		  {
			    			  if(isMax)
			    			  {
			    				  if(playersCount[0] > optimum[1])
			    				  {
			    					  optimum[0] = i;
			    					  optimum[1] = playersCount[0];
			    				  }
			    			  }
			    			  else
			    			  {
			    				  if(optimum[1] == -1)
			    				  {
			    					  optimum[0] = i;
			    					  optimum[1] = playersCount[0];
			    				  }
			    				  else
			    				  {
			    					  if(playersCount[0] < optimum[1])
			    					  {
				    					  optimum[0] = i;
			    						  optimum[1] = playersCount[0];
			    					  }
			    				  }
			    			  }
			    		  }
			    		  else if(turn == 2)
			    		  {
			    			  if(isMax)
			    			  {
			    				  if(playersCount[1] > optimum[1])
			    				  {
			    					  optimum[0] = i;
			    					  optimum[1] = playersCount[1];
			    				  }
			    			  }
			    			  else
			    			  {
			    				  if(optimum[1] == -1)
			    				  {
			    					  optimum[0] = i;
			    					  optimum[1] = playersCount[1];
			    				  }
			    				  else
			    				  {
			    					  if(playersCount[1] < optimum[1])
			    					  {
				    					  optimum[0] = i;
			    						  optimum[1] = playersCount[1];
			    					  }
			    				  }
			    			  }
			    		  }
	    			  }
	    			  else
	    			  {
	    				  if(isMax)
	    				  {
	    					  if(minimaxResult[1] > optimum[1])
	    					  {
	    						  optimum = minimaxResult;
	    					  }
	    				  }
	    				  else
	    				  {
	    					  if(optimum[1] == -1)
	    					  {
	    						  optimum = minimaxResult;
	    					  }
	    					  else
	    					  {
	    						  if(minimaxResult[1] < optimum[1])
	    						  {
	    							  optimum = minimaxResult;
	    						  }
	    					  }
	    				  }
	    			  }
	    		  }
	    		  
	    	  }
	    	  
	    	  return optimum;
	      }
	      
	      
	      // Applies the necessary changes to a fake board (used for further calculations in different strategy functions)
	      private Cell[][] TempApplyChange(Cell[][] gameCells,int i,int j,int turn)
	      {
	    	  int m;
	    	  int n;
	    	  int opponent;
	    	  boolean isChain = false;
	    	  ArrayList<int[]> myList = new ArrayList<int[]>();
	    	  int[] temp;
	    	  
	    	  if(turn == 1)
	    		  opponent = 2;
	    	  else
	    		  opponent = 1;
	    	  
	    	  gameCells[i][j].state = turn;
	    	  
    		  for(int k = i-1;k <= i+1; k++)
    		  {
    	    	  for(int l=j-1;l <= j+1;l++)
				  {
    	    		  // Copying k,l to m and n, so we don't loose their value in the calculation
    	    		  m = k;
    	    		  n = l;
    	    		  isChain = false;
    	    		  myList.clear();
    	    		  temp = new int[2];
    	    		  while(m>=0 && m<8 && n>=0 && n<8)
    	    		  {
    	    			  if(m+(k-i)>=0 && m+(k-i)<8 && n+(l-j)>=0 && n+(l-j)<8)
    	    			  {
    	    				  if(gameCells[m][n].state == opponent && gameCells[m+(k-i)][n+(l-j)].state == turn)
    	    	    		  {
    	    					  if(!isChain)
    	    					  {
    	    						  gameCells[m][n].state = turn;
    	    				    	  break;
    	    					  }
    	    					  else
    	    					  {
    	    						  for(int p=0;p < myList.size();p++)
    	    						  {
    	    							  gameCells[myList.get(p)[0]][myList.get(p)[1]].state = turn;
    	    						  }
    	    						  gameCells[m][n].state = turn;
    	    				    	  break;
    	    					  }
    	    	    		  }
    	    				  else if(gameCells[m][n].state == opponent && gameCells[m+(k-i)][n+(l-j)].state == opponent)
    	    				  {
    	        	    		  temp = new int[2];
    	    					  temp[0] = m; temp[1] = n;
    	    					  myList.add(temp);
    	    					  isChain = true;
    	    					  m += k-i;
    	    					  n += l-j;
    	    				  }
    	    				  else
    	    				  {
    	    					  break;
    	    				  }
    	    			  }
    	    			  else
    	    			  {
    	    				  break;
    	    			  }
    	    		  }
				  }
    		  }
    		  return gameCells;
	      }
	      
	      // Calculate the possible moves for a player
	      public static int PossibleMoves(Cell[][] gameCells,int turn)
	      {
	    	  int k = 0;
	    	  
	    	  for(int i=0;i<20;i++)
	    	  {
	    		  possibleCells[i][0] = -1;
	    		  possibleCells[i][1] = -1;
	    	  }
	    	  
	    	  for(int i=0;i<8;i++)
	    	  {
	    		  for(int j=0;j<8;j++)
	    		  {
	    			  if(gameCells[i][j].state == 0 && IsMoveValid(gameCells,i,j,turn))
	    			  {
	    				  possibleCells[k][0] = i;
	    				  possibleCells[k][1] = j;
	    				  k++;
	    			  }
	    		  }
	    	  }
	    	  return k;
	      }
	      
	      // Checks whether the player's choice is a valid move or not, by checking the eight  cells around it
	      private static boolean IsMoveValid(Cell[][] gameCells,int i,int j,int turn)
	      {
	    	  boolean validation = false;
	    	  
     		  for(int k = i-1;k <= i+1; k++)
    		  {
    	    	  for(int l=j-1;l <= j+1;l++)
				  {
    	    		  if(k>=0 && k<8 && l>=0 && l<8)
    	    		  {
    	    			  if(k+(k-i)>=0 && k+(k-i)<8 && l+(l-j)>=0 && l+(l-j)<8)
    	    			  {
    	    				  validation = IsMoveValid(gameCells,i,j,k,l,turn);
    	    				  if(validation)
    	    				  {
    	    					  return true;
    	    				  }
    	    			  }
    				  }
    	    	  }
    		  }

	    	  return false;
	      }
	      
	      
	      // Counts how many cell is taken by each player
	      private int[] BoardCount(Cell[][] gameCells)
	      {
	    	  int[] count = new int[2];
	    	  
	    	  count[0] = 0;
	    	  count[1] = 0;
	    	  
	    	  for(int i=0;i<8;i++)
	    	  {
	    		  for(int j=0;j<8;j++)
	    		  {
	    			  if(gameCells[i][j].state == 1)
	    			  {
	    				  count[0]++;
	    			  }
	    			  if(gameCells[i][j].state == 2)
	    			  {
	    				  count[1]++;
	    			  }
	    		  }
	    	  }
	    	  
	    	  return count;
	      }
	      
	      //Checks whether the game is finished or not
	      private boolean IsGameFinished(Cell[][] gameCells)
	      {
	    	  int cellCounter = 0;
	    	  while(cellCounter < 64)
	    	  {
	    		  int x = cellCounter / 8;
	    		  int y = cellCounter % 8;
	    		  if(gameCells[x][y].state == 0)
	    		  {
	    			  if(PossibleMoves(cells,1)!= 0 || PossibleMoves(cells,2)!= 0)
	    				  return false;
	    		  }
	    	  }
	    	  return true;
	      }
