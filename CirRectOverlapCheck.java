public class CirRectOverlapCheck
{
    // (Xc, Yc) is the centre of the circle. (X1, Y1) & (X2, Y2) are the opposite diagonal points of the rectangle. Any of the two pairs can be chosen.
    boolean checkOverlap(int R, int Xc, int Yc, int X1, int Y1, int X2, int Y2)
    {
        // (Xn, Yn) is the nearest point to the circle on the rectangle.
        int Xn = Math.max(X1, Math.min(Xc, X2));
        int Yn = Math.max(Y1, Math.min(Yc, Y2));
         
        // Distance b/w the centre of the circle and the "nearest point" computed above.
        return ((Xn - Xc) * (Xn - Xc) + (Yn - Yc) * (Yn - Yc)) <= R * R;
    }
}

// Reference: https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/