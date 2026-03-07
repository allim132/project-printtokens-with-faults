
// @wei Jack-Printtokens2.java
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Printtokens2 {
	static int error = 0;
	static int keyword = 1;
	static int spec_symbol = 2;
	static int identifier = 3;
	static int num_constant = 41;
	static int str_constant = 42;
	static int char_constant = 43;
	static int comment = 5;
	
	// 1 *fault detected, function should return br (no correction yet)
	/***********************************************/
	/* NMAE:	open_character_stream          */
	/* INPUT:       a filename                     */
	/* OUTPUT:      a BufferedReader */
	/* DESCRIPTION: when not given a filename,     */
	/*              open stdin,otherwise open      */
	/*              the existed file               */
	/***********************************************/
	BufferedReader open_character_stream(String fname) {						// 1
		BufferedReader br = null;												// 2
		if (fname == null) {													// 3
			br = new BufferedReader(new InputStreamReader(System.in));			// 4
		} else {																// 5
			try {																// 6
				FileReader fr = new FileReader(fname);							// 7
				br = new BufferedReader(fr);									// 8
			} catch (FileNotFoundException e) {									// 9
				System.out.print("The file " + fname +" doesn't exists\n");		// 10
				e.printStackTrace();											// 11
			}																	// 12
		}																		// 13
																				// 14
		return br; 															// 15 Note: should return br
	}
	
	// 2	*fault: on error state, does not actually return -1 on EOF
	/**********************************************/
	/* NAME:	get_char                      */
	/* INPUT:       a BufferedReader      */
	/* OUTPUT:      a character (f2,remove"when EOF, return -1" in the comment) */
	/**********************************************/
	int get_char(BufferedReader br){ 											// 1
            int ch = 0;															// 2
	    try {																	// 3
	    	br.mark(3); 														// 4 why is line 6 not in the try clause?
		ch= br.read();															// 5 what's going on over here?
		} catch (IOException e) {												// 6
			e.printStackTrace();												// 7
			return -1;															// 7.5, code correction, returns -1 on IOException
		}																		// 8
	    return ch;																// 9
	}													
	
	


	// 3	*fault: does not actually output a character
	/***************************************************/
	/* NAME:      unget_char                           */
	/* INPUT:     a BufferedReader,a character */
	/* OUTPUT:    a character                          */
	/* DESCRIPTION: move backward  */
	/***************************************************/
	char unget_char (int ch,BufferedReader br) { 								// 1
	  try {																		// 2
		br.reset();																// 3 
	} catch (IOException e) {													// 4
		e.printStackTrace();													// 5
	}																			// 6
		 return (char)ch;																// 7	code correction applied, now returns char 
	}		
	
	// 4
	/********************************************************/
	/* NAME:	open_token_stream                       */
	/* INPUT:       a filename                              */
	/* OUTPUT:      a BufferedReader             */
	/* DESCRIPTION: when filename is EMPTY,choice standard  */
	/*              input device as input source            */
	/********************************************************/
	BufferedReader open_token_stream(String fname)								// 1
	{																			// 2
		BufferedReader br;														// 3
	 if(fname == null || fname.isEmpty()) 													// 4	fault correction applied, correct null comparison and empty correct empty check
	    br=open_character_stream(null);									// 5	Does this really set the standard input device as input source?
	 else																		// 6
	    br=open_character_stream(fname);										// 7
	 return br;																	// 8
	}
	
	// 5
	/********************************************************/
	/* NAME :	get_token                               */
	/* INPUT: 	a BufferedReader          */
	/* OUTPUT:      a token string                                */
	/* DESCRIPTION: according the syntax of tokens,dealing  */
	/*              with different case  and get one token  */
	/********************************************************/
	String get_token(BufferedReader br)											// 1
	{ 																			// 2
	  // int i=0,j;																// 3   What are these used for? 	fault correction
	  int id=0;																	// 4
	  int res = 0;																// 5   Why is res an int here? -> perhaps this is necessary
	  char ch = '\0';															// 6

	  StringBuilder sb = new StringBuilder();									// 7



	   try {																	// 8
		   res = get_char(br);													// 9
		   if (res == -1) {														// 10
			   return null;														// 11
		   }																	// 12
		   ch = (char)res;														// 13
		while(ch=='\t'||ch=='\n' || ch == '\r')     /* strip all blanks until meet characters */ // 14  
	      {																		// 15
			res = get_char(br);													// 16
			ch = (char)res;														// 17
	      } 																	// 18
	   
	   if(res == -1)return null;												// 19
	   sb.append(ch);															// 20
	   if(is_spec_symbol(ch)==true)return sb.toString(); 						// 21
	   if(ch =='"')id=2;    /* prepare for string */  							// 22  Are these exhausted of all the cases?
	   if(ch ==59)id=1;    /* prepare for comment */    						// 23  Are these exhausted of all the cases?

	   res = get_char(br);														// 24
	   if (res == -1) {															// 25
		   //unget_char(ch,br);													// 26	fault correction: -1 implies that we've reach EOF, but we still get the char, thus we must remove this code
		   return sb.toString();												// 27
	   }																		// 28
	   ch = (char)res;															// 29

	   while (is_token_end(id,res) == false)/* until meet the end character */	// 30  Does this really correctly parse until end char? What is end char?
	   {
	       sb.append(ch);														// 31
	       br.mark(4);															// 32
	       res = get_char(br);													// 33
		   if (res == -1) {														// 34
			   break;															// 35
		   }																	// 36
		   ch = (char)res;														// 37
	   }																		// 38
	 
	   if(res == -1)       /* if end character is eof token    */				// 39
	      { //unget_char(ch,br);        /* then put back eof on token_stream */	// 40	fault correction: -1 implies EOF, and ch is a char and chars cannot store EOF
	        return sb.toString();												// 41
	      }																		// 42
	 
	   if(is_spec_symbol(ch)==true)     /* if end character is special_symbol */// 43
	      { unget_char(ch,br);        /* then put back this character       */	// 44
	        return sb.toString();												// 45
	      }																		// 46
	   if(id==1)                  /* if end character is " and is string */		// 47
	     {                     													// 48
	       sb.append(ch);														// 49
	       return sb.toString(); 												// 50
	     }																		// 51
	   if(id==0 && ch==59)														// 52
	                                   /* when not in string or comment,meet ";"// 53 */
	     { unget_char(ch,br);       /* then put back this character         */	// 54
	       return sb.toString(); 												// 55
	     }																		// 56
	} catch (IOException e) {													// 57
		e.printStackTrace();													// 58
	}																			// 59

	   return sb.toString();                   /* return nomal case token       // 60      */
	}																			// 61
	
	// 6
	/*******************************************************/
	/* NAME:	is_token_end                           */
	/* INPUT:       a character,a token status             */
	/* OUTPUT:	a BOOLEAN value                        */
	/*******************************************************/
	static boolean is_token_end(int str_com_id, int res)						// 1
	{																			// 2
	 if(res==-1)return(true); /* is eof token? */								// 3
	 char ch = (char)res;														// 4
	 if(str_com_id==1)          /* is string token */							// 5
	    { if(ch=='"' || ch=='\n' || ch == '\r')   /* for string until meet anothe// 6 r " */	// fault correction: single |, changed to ||
	         return true;														// 7
	      else																	// 8
	         return false;														// 9
	    }																		// 10

	 if(str_com_id==2)    /* is comment token */								// 11
	   { if(ch=='\n' || ch == '\r' || ch=='\t')     /* for comment until meet en// 12 d of line */ 	// Why is do comments care for \n, \r, or \t?
	        return true;														// 13
	      else																	// 14
	        return false;														// 15
	   }																		// 16

	 if(is_spec_symbol(ch)==true) return true; /* is special_symbol? */			// 17
	 if(ch ==' ' || ch=='\n'|| ch=='\r' || ch==59) return true; 				// 18 				// Why checking these characters
	               
	 return false;               /* other case,return FALSE */					// 19
	}
	
	// 7
	/****************************************************/
	/* NAME :	token_type                          */
	/* INPUT:       a token              */
	/* OUTPUT:      an integer value                    */
	/* DESCRIPTION: the integer value is corresponding  */
	/*              to the different token type         */
	/****************************************************/
	static int token_type(String tok)											// 1
	{ 																			// 2
	 if(is_keyword(tok))return(keyword);										// 3
	 if(is_spec_symbol(tok.charAt(0)))return(spec_symbol);						// 4
	 if(is_identifier(tok))return(identifier);									// 5
	 if(is_num_constant(tok))return(num_constant);								// 6
	 if(is_str_constant(tok))return(str_constant);								// 7
	 if(is_char_constant(tok))return(char_constant);							// 8
	 if(is_comment(tok))return(comment);										// 9
	 return(error);                    /* else look as error token */			// 10
	}
	
	// 8	// I would personally add a final type check for illegal types. Otherwise it literally prints nothing
	/****************************************************/
	/* NAME:	print_token                             */
	/* INPUT:	a token                                 */
	/****************************************************/
	void print_token(String tok)												// 1
	{ int type;																	// 2
	  type=token_type(tok);														// 3
	 if(type==error)															// 4
	   { 																		// 5
	   	System.out.print("error,\"" + tok + "\".\n");							// 6
	   }																		// 7
	   
	 if(type==keyword)															// 8
	   {																		// 9
	   System.out.print("keyword,\"" + tok + "\".\n");							// 10
	   }																		// 11

	 if(type==spec_symbol)print_spec_symbol(tok); 								// 12
	 if(type==identifier)														// 13
	   {																		// 14
	   System.out.print("identifier,\"" + tok + "\".\n");						// 15
	   }																		// 16
	 if(type==num_constant)														// 17
	   {																		// 18
	   System.out.print("numeric," + tok + ".\n");								// 19
	   }																		// 20
	
	   // fault correction, missing case for str_constant
	   if(type == str_constant)
		{
			System.out.print("string," + tok + ".\n");
		}

		// fault correction, missing case for comment
		if(type == comment)
		{
			System.out.print("comment," + tok + ".\n");
		}
	 
	 if(type==char_constant)													// 21
	   {																		// 22
	    System.out.print("character,\"" + tok.charAt(1) + "\".\n");				// 23
	   }																		// 24

	   }																		// 25

	/* the code for tokens judgment function */

	// 9 	possible issue: The function accesses ident.charAt(0) without checking whether ident is null or empty, which can cause runtime exceptions.
	/*************************************/
	/* NAME:	is_comment           */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_comment(String ident)										// 1
	{																			// 2
	  if( ident.charAt(0) ==59 )   /* the char is 59   */						// 3  		// Why is it checking char 59 which is a ; for if something is a comment?
	     return true;															// 4
	  else																		// 5
	     return false;															// 6
	}																			// 7
	
	/*************************************/
	/* NAME:	is_keyword           */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_keyword(String str)
	{ 
	if (str.equals("and") || str.equals("or") || str.equals("if") ||
			 str.equals("xor")||str.equals("lambda")||str.equals("=>"))
	      return true;
	  else 
	      return false;
	}
	
	/*************************************/
	/* NAME:	is_char_constant     */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_char_constant(String str)
	{
	  if (str.length() == 2 && str.charAt(0)=='#' && Character.isLetter(str.charAt(1))) // changed > to ==  
	     return true;
	  else  
	     return false;
	}
	
	/*************************************/
	/* NAME:	is_num_constant      */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
     
	static boolean is_num_constant(String str)
	{
	  int i=1;
        if (str == null || str.length() == 0)
            return false;                       // added this null/empty check to take case of empty or null cases
	  if ( Character.isDigit(str.charAt(0))) 
	    {
            // while (i <= str.length() && str.charAt(i) != '\0') is unsafe because str.charAt(i) will fail when i == str.length() and str.charAt(i+1) can also go out of bounds also Java strings do not use '\0' as an ending marker like C strings. Below is the corrected code.
	        while (i < str.length())
            {
                if (Character.isDigit(str.charAt(i)))
                    i++;
                else
                    return false;
            }
        return true;
	    }
	  else
	   return false;               /* other return FALSE */
	}

	/*************************************/
	/* NAME:	is_str_constant      */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_str_constant(String str)
	{
	  int i=1;
      if (str == null || str.length() == 0)
        return false;                            // this takes care of empty string

      if (str.charAt(0) == '"') // fixed the format modified 
        {
            while (i < str.length())
            {
                if (str.charAt(i) == '"' && i == str.length() - 1)
                    return true;
                else
                    i++;
            }
            return false;
        }
        else
            return false;
    }
	
	/*************************************/
	/* NAME:	is_identifier         */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/ // this code has no issues in it
	static boolean is_identifier(String str)
	{
	  int i=0; 

	  if ( Character.isLetter(str.charAt(0)) ) 
	     {
	        while(i < str.length())   /* unti meet the end token sign */ // what it had before was for c not java
	           { 
	            if(Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i)))   
	               i++;
	            else
	               return false;
	           }      /* end WHILE */
	     return true; // changed to true from false
	     }
	  else
	     return false; // changed to false from true 
	}
	
	/******************************************/
	/* NAME:	unget_error               */
	/* INPUT:      a BufferedReader */
	/* OUTPUT: 	print error message       */
	/******************************************/ // this code was also correct
	static void unget_error(BufferedReader br)
	{
		System.out.print("It can not get character\n");
	}
	
	/*************************************************/
	/* NAME:        print_spec_symbol                */
	/* INPUT:       a spec_symbol token */
	/* OUTPUT :     print out the spec_symbol token  */
	/*              according to the form required   */
	/*************************************************/
	static void print_spec_symbol(String str)
	{
	    if      (str.equals("(")) // changed {  to (
	    {
	         
	             System.out.print("lparen.\n");
	             return;
	    } 
	    if (str.equals(")"))
	    {
	      
	             System.out.print("rparen.\n");
	             return;
	    }
	    if (str.equals("["))
	    {
	             System.out.print("lsquare.\n");
	             return;
	    }
	    if (str.equals("]"))
	    {
	       
	             System.out.print("rsquare.\n");
	             return;
	    }
	    if (str.equals("'"))
	    {
	             System.out.print("quote.\n");
	             return;
	    }
	    if (str.equals("`"))
	    {
	 
	             System.out.print("bquote.\n");
	             return;
	    }
	    
	    
	}
	
	/*************************************/
	/* NAME:        is_spec_symbol       */
	/* INPUT:       a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/ // this method is correct as well
	static boolean is_spec_symbol(char c)
	{
	    if (c == '(')
	    {  
	        return true;
	    }
	    if (c == ')')
	    {
	        return true;
	    }
	    if (c == '[')
	    {
	        return true;
	    }
	    if (c == ']')
	    {
	        return true;
	    }
	    if (c == '/') 
	    {
	        return true;
	    }
	    if (c == '`')
	    {
	        return true;
	    }
	    if (c == ',')
	    {
	        return true;
	    }
	    return false;     /* others return FALSE */
	}
	
	public static void main(String[] args) throws IOException {
		String fname = null;                     
		if (args.length == 0) {	/* if not given filename,take as '""' */
			fname = null;                    // ****************************************************** fixed this it was fname= new string()
		} else if (args.length == 1) {
			fname = args[0];           //*********************************************args[1] */
		} else {
			System.out.print("Error!,please give the token stream\n");
			System.exit(0);
		}
		Printtokens2 t = new Printtokens2();
		BufferedReader br = t.open_token_stream(fname);	/* open token stream */
        if (br == null) {
            System.out.print("Error opening input stream\n");
            System.exit(0);
        }
		String tok = t.get_token(br);
		while (tok != null) {	/* take one token each time until eof */
			t.print_token(tok);
			tok = t.get_token(br);
		}
		
		System.exit(0);
	}
}