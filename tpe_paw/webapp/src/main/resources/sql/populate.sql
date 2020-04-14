/*
    TO manually populate database in terminal: psql -U postgres -d paw -a -f populate.sql
 */
delete from snippets where true;
delete from votes_for where true;
delete from favorites where true;
delete from follows where true;
delete from snippet_tags where true;
delete from languages where true;
delete from tags where true;
delete from users where true;


insert into users(username, password, email, description, reputation, date_joined) values('JohnDoe','password','johndoe@gmail.com', 'I am the first user created on this website, I own many snippets', 0, timestamp '2019-09-28 12:30:00');
insert into users(username, password, email, description, reputation, date_joined) values('JaneRoe','password','janeroe@gmail.com', 'I am just visiting', 0, timestamp '2019-09-28 12:30:00');

insert into languages(name) values('java');
insert into languages(name) values('c#');
insert into languages(name) values('python');
insert into languages(name) values('javascript');
insert into languages(name) values('c');
insert into languages(name) values('css');

insert into tags(name) values('sorting');
insert into tags(name) values('sort');
insert into tags(name) values('greedy');
insert into tags(name) values('performance');
insert into tags(name) values('math');
insert into tags(name) values('machine-learning');
insert into tags(name) values('style');

insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Implementation of merge sort','This is an implementation of merge sort in java',
'class MergeSort
{
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(int arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int [n1];
        int R[] = new int [n2];

        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];


        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (L[i] <= R[j])
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    void sort(int arr[], int l, int r)
    {
        if (l < r)
        {
            // Find the middle point
            int m = (l+r)/2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr , m+1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    // Driver method
    public static void main(String args[])
    {
        int arr[] = {12, 11, 13, 5, 6, 7};

        System.out.println("Given Array");
        printArray(arr);

        MergeSort ob = new MergeSort();
        ob.sort(arr, 0, arr.length-1);

        System.out.println("\nSorted array");
        printArray(arr);
    }
} ', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Get name of current method','Get the name of the current method, might not be classy, but it works','String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Meassure time','Surround your algorithm/function with this Stopwatch in order to meassure the time it takes',
'using System.Diagnostics;
// ...

Stopwatch sw = new Stopwatch();

sw.Start();

// ...

sw.Stop();

Console.WriteLine("Elapsed={0}",sw.Elapsed);
', timestamp '2020-04-10 12:40:00', (select id from languages where name='c#'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Dijkstra Shortest Path','Implementation of Dijkstras algorithm for the shortest path in graphs, this is a greedy algorithm',
'import java.util.*; 
import java.lang.*; 
import java.io.*; 
  
class ShortestPath { 
    // A utility function to find the vertex with minimum distance value, 
    // from the set of vertices not yet included in shortest path tree 
    static final int V = 9; 
    int minDistance(int dist[], Boolean sptSet[]) 
    { 
        // Initialize min value 
        int min = Integer.MAX_VALUE, min_index = -1; 
  
        for (int v = 0; v < V; v++) 
            if (sptSet[v] == false && dist[v] <= min) { 
                min = dist[v]; 
                min_index = v; 
            } 
  
        return min_index; 
    } 
  
    // A utility function to print the constructed distance array 
    void printSolution(int dist[]) 
    { 
        System.out.println("Vertex \t\t Distance from Source"); 
        for (int i = 0; i < V; i++) 
            System.out.println(i + " \t\t " + dist[i]); 
    } 
  
    // Function that implements Dijkstras single source shortest path 
    // algorithm for a graph represented using adjacency matrix 
    // representation 
    void dijkstra(int graph[][], int src) 
    { 
        int dist[] = new int[V]; // The output array. dist[i] will hold 
        // the shortest distance from src to i 
  
        // sptSet[i] will true if vertex i is included in shortest 
        // path tree or shortest distance from src to i is finalized 
        Boolean sptSet[] = new Boolean[V]; 
  
        // Initialize all distances as INFINITE and stpSet[] as false 
        for (int i = 0; i < V; i++) { 
            dist[i] = Integer.MAX_VALUE; 
            sptSet[i] = false; 
        } 
  
        // Distance of source vertex from itself is always 0 
        dist[src] = 0; 
  
        // Find shortest path for all vertices 
        for (int count = 0; count < V - 1; count++) { 
            // Pick the minimum distance vertex from the set of vertices 
            // not yet processed. u is always equal to src in first 
            // iteration. 
            int u = minDistance(dist, sptSet); 
  
            // Mark the picked vertex as processed 
            sptSet[u] = true; 
  
            // Update dist value of the adjacent vertices of the 
            // picked vertex. 
            for (int v = 0; v < V; v++) 
  
                // Update dist[v] only if is not in sptSet, there is an 
                // edge from u to v, and total weight of path from src to 
                // v through u is smaller than current value of dist[v] 
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) 
                    dist[v] = dist[u] + graph[u][v]; 
        } 
  
        // print the constructed distance array 
        printSolution(dist); 
    } 
  
    // Driver method 
    public static void main(String[] args) 
    { 
        /* Let us create the example graph discussed above */
        int graph[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, 
                                      { 4, 0, 8, 0, 0, 0, 0, 11, 0 }, 
                                      { 0, 8, 0, 7, 0, 4, 0, 0, 2 }, 
                                      { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, 
                                      { 0, 0, 0, 9, 0, 10, 0, 0, 0 }, 
                                      { 0, 0, 4, 14, 10, 0, 2, 0, 0 }, 
                                      { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, 
                                      { 8, 11, 0, 0, 0, 0, 1, 0, 7 }, 
                                      { 0, 0, 2, 0, 0, 0, 6, 7, 0 } }; 
        ShortestPath t = new ShortestPath(); 
        t.dijkstra(graph, 0); 
    } 
} 
', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Heapsort','Implementation of heapsort in java without a Heap class',
'public class HeapSort 
{ 
    public void sort(int arr[]) 
    { 
        int n = arr.length; 
  
        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapify(arr, n, i); 
  
        // One by one extract an element from heap 
        for (int i=n-1; i>0; i--) 
        { 
            // Move current root to end 
            int temp = arr[0]; 
            arr[0] = arr[i]; 
            arr[i] = temp; 
  
            // call max heapify on the reduced heap 
            heapify(arr, i, 0); 
        } 
    } 
  
    // To heapify a subtree rooted with node i which is 
    // an index in arr[]. n is size of heap 
    void heapify(int arr[], int n, int i) 
    { 
        int largest = i; // Initialize largest as root 
        int l = 2*i + 1; // left = 2*i + 1 
        int r = 2*i + 2; // right = 2*i + 2 
  
        // If left child is larger than root 
        if (l < n && arr[l] > arr[largest]) 
            largest = l; 
  
        // If right child is larger than largest so far 
        if (r < n && arr[r] > arr[largest]) 
            largest = r; 
  
        // If largest is not root 
        if (largest != i) 
        { 
            int swap = arr[i]; 
            arr[i] = arr[largest]; 
            arr[largest] = swap; 
  
            // Recursively heapify the affected sub-tree 
            heapify(arr, n, largest); 
        } 
    } 
  
    /* A utility function to print array of size n */
    static void printArray(int arr[]) 
    { 
        int n = arr.length; 
        for (int i=0; i<n; ++i) 
            System.out.print(arr[i]+" "); 
        System.out.println(); 
    } 
  
    // Driver program 
    public static void main(String args[]) 
    { 
        int arr[] = {12, 11, 13, 5, 6, 7}; 
        int n = arr.length; 
  
        HeapSort ob = new HeapSort(); 
        ob.sort(arr); 
  
        System.out.println("Sorted array is"); 
        printArray(arr); 
    } 
} 
', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Append text to file','Append text to a file in java, have to be careful with th exception',
'BufferedWriter out = null;
try {
	out = new BufferedWriter(new FileWriter(”filename”, true));
	out.write(”aString”);
} catch (IOException e) {
	// error processing code
} finally {
	if (out != null) {
		out.close();
	}
}
', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'PDF Generation with iText','Code to generate a PDF in Java using the iText library, heres the link: https://itextpdf.com/en/resources/api-documentation',
'import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class GeneratePDF {

    public static void main(String[] args) {
        try {
            OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));

            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            document.add(new Paragraph("Hello Kiran"));
            document.add(new Paragraph(new Date().toString()));

            document.close();
            file.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Sigmoid Function','Implementation of a simple sigmoid function in Python',
'import math

def sigmoid(x):
  return 1 / (1 + math.exp(-x))
', timestamp '2020-01-20 09:40:00', (select id from languages where name='python'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Heuns method','Implementation of Heuns method in Python, includes a plotting of the function',
'import matplotlib.pyplot as plt
import math

def feval(funcName, *args):
    return eval(funcName)(*args)

def mult(vector, scalar):
    newvector = [0]*len(vector)
    for i in range(len(vector)):
        newvector[i] = vector[i]*scalar
    return newvector

def HeunsMethod(func, yinit, x_range, h):
    numOfODEs = len(yinit)
    sub_intervals = int((x_range[-1] - x_range[0])/h)
    
    x = x_range[0]
    y = yinit
    
    # Containers for solutions
    xsol = [x]
    ysol = [y[0]]

    for i in range(sub_intervals):
        y0prime = feval(func, x, y)

        k1 = mult(y0prime, h)

        ypredictor = [u + v for u, v in zip(y, k1)]

        y1prime = feval(func, x+h, ypredictor)

        for j in range(numOfODEs):
            y[j] = y[j] + (h/2)*y0prime[j] + (h/2)*y1prime[j]

        x = x + h
        xsol.append(x)

        for r in range(len(y)):
            ysol.append(y[r])

    return [xsol, ysol]

def myFunc(x, y):
    dy = [0]*len(y)
    dy[0] = 3*(1+x) - y[0]
    return dy

# -----------------------

h = 0.2
x = [1, 2]
yinit = [4]

[ts, ys] = HeunsMethod("myFunc", yinit, x, h)

# For system of ODEs:
#numOfODEs = len(yinit)
#ys1 = ys[0::numOfODEs]
#ys2 = ys[1::numOfODEs]
#etc

dt = int((x[-1] - x[0]) / h)
t = [x[0]+i*h for i in range(dt+1)]
yexact = []
for i in range(dt+1):
    ye = 3 * t[i] + math.exp(1 - t[i])
    yexact.append(ye)
    
plt.plot(ts, ys, "r")
plt.plot(t, yexact, "b")
plt.xlim(x[0], x[1])
plt.legend(["Heun"s method", "Exact solution"], loc=2)
plt.xlabel("x", fontsize=17)
plt.ylabel("y", fontsize=17)
plt.tight_layout()
plt.show()

# Uncomment the following to print the figure:
#plt.savefig("Fig_ex1_HeunsMethod.png", dpi=600)
', timestamp '2020-01-20 09:40:00', (select id from languages where name='python'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Text Blur','End of text blur in CSS to make it look like there is more text',
'<div class="sidebar-box">
  <p>malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.</p>
  <p class="read-more"><a href="#" class="button">Read More</a></p>
</div>

.sidebar-box {
  max-height: 120px;
  position: relative;
  overflow: hidden;
}
.sidebar-box .read-more { 
  position: absolute; 
  bottom: 0; 
  left: 0;
  width: 100%; 
  text-align: center; 
  margin: 0; padding: 30px 0; 
	
  /* "transparent" only works here because == rgba(0,0,0,0) */
  background-image: linear-gradient(to bottom, transparent, black);
}
', timestamp '2020-02-15 21:47:12', (select id from languages where name='css'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='JohnDoe'),'Netflix-like Card Expansion','On hover, cards expand and push other cards to the side. It is similar to the effect Netflix uses',
'<div class="container">
  <a href="#animals" class="item"><img src="https://placeimg.com/640/480/animals" alt="Animals"></a>
  <a href="#architecture" class="item"><img src="https://placeimg.com/640/480/architecture" alt="Architecture"></a>
  <a href="#nature" class="item"><img src="https://placeimg.com/640/480/nature" alt="Nature"></a>
  <a href="#people" class="item"><img src="https://placeimg.com/640/480/people" alt="People"></a>
  <a href="#tech" class="item"><img src="https://placeimg.com/640/480/tech" alt="Tech"></a>
</div>

.container {
  display: flex;
  margin-top: 50px;
}

.item {
  position: relative;
  display: block;
  flex: 1 1 0px;
  transition: transform 500ms;
}

.container:focus-within .item,
.container:hover .item {
  transform: translateX(-25%);
}

.item:focus ~ .item,
.item:hover ~ .item {
  transform: translateX(25%);
}

.container .item:focus,
.container .item:hover {
  transform: scale(1.5);
  z-index: 1;
}

body {
  overflow: hidden;
}

.item img {
  display: block;
  max-width: 100%;
}
', timestamp '2020-03-01 03:30:12', (select id from languages where name='css'));

insert into votes_for(user_id, snippet_id, type) values((select id from users where username='JaneRoe'),(select id from snippets where title='Implementation of merge sort'), 1);
insert into votes_for(user_id, snippet_id, type) values((select id from users where username='JaneRoe'),(select id from snippets where title='Dijkstra Shortest Path'), 1);
insert into votes_for(user_id, snippet_id, type) values((select id from users where username='JaneRoe'),(select id from snippets where title='Netflix-like Card Expansion'), 1);

insert into favorites(snippet_id,user_id) values((select id from snippets where title='Heapsort'),(select id from users where username='JaneRoe'));
insert into favorites(snippet_id,user_id) values((select id from snippets where title='Implementation of merge sort'),(select id from users where username='JaneRoe'));
insert into favorites(snippet_id,user_id) values((select id from snippets where title='Sigmoid Function'),(select id from users where username='JaneRoe'));

insert into follows(user_id,tag_id) values((select id from users where username='JaneRoe'),(select id from tags where name='sort'));
insert into follows(user_id,tag_id) values((select id from users where username='JaneRoe'),(select id from tags where name='math'));
insert into follows(user_id,tag_id) values((select id from users where username='JaneRoe'),(select id from tags where name='style'));

insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Meassure time'),(select id from tags where name='performance'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Implementation of merge sort'),(select id from tags where name='sorting'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Heapsort'),(select id from tags where name='sorting'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Implementation of merge sort'),(select id from tags where name='sort'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Heapsort'),(select id from tags where name='sort'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Dijkstra Shortest Path'),(select id from tags where name='greedy'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Heuns method'),(select id from tags where name='math'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Sigmoid Function'),(select id from tags where name='math'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Sigmoid Function'),(select id from tags where name='machine-learning'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Text Blur'),(select id from tags where name='style'));
insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Netflix-like Card Expansion'),(select id from tags where name='style'));