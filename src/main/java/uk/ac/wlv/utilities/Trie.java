package uk.ac.wlv.utilities;

/**
 * 单词查找树
 *
 * @author 13986
 * @date 2023/03/06
 */
public class Trie {
   /**
    * 获得字符串的单词查找树位置
    *
    * @param sText             文本
    * @param sArray            数组
    * @param iLessPointer      少指针
    * @param iMorePointer      多指针
    * @param iFirstElement     第一个元素
    * @param iLastElement      最后一个元素
    * @param bDontAddNewString 不添加新字符串
    * @return int
    */
   public static int i_GetTriePositionForString(String sText, String[] sArray, int[] iLessPointer, int[] iMorePointer, int iFirstElement, int iLastElement, boolean bDontAddNewString) {
      int iTriePosition = 0;
      int iLastTriePosition = 0;
      if (iLastElement < iFirstElement) {
         sArray[iFirstElement] = sText;
         iLessPointer[iFirstElement] = -1;
         iMorePointer[iFirstElement] = -1;
         return iFirstElement;
      } else {
         iTriePosition = iFirstElement;

//         int iLastTriePosition;
         label33:
         do {
            do {
               iLastTriePosition = iTriePosition;
               if (sText.compareTo(sArray[iTriePosition]) < 0) {
                  iTriePosition = iLessPointer[iTriePosition];
                  continue label33;
               }

               if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                  return iTriePosition;
               }

               iTriePosition = iMorePointer[iTriePosition];
            } while(iTriePosition != -1);

            if (bDontAddNewString) {
               return -1;
            }

            ++iLastElement;
            sArray[iLastElement] = sText;
            iLessPointer[iLastElement] = -1;
            iMorePointer[iLastElement] = -1;
            iMorePointer[iLastTriePosition] = iLastElement;
            return iLastElement;
         } while(iTriePosition != -1);

         if (bDontAddNewString) {
            return -1;
         } else {
            ++iLastElement;
            sArray[iLastElement] = sText;
            iLessPointer[iLastElement] = -1;
            iMorePointer[iLastElement] = -1;
            iLessPointer[iLastTriePosition] = iLastElement;
            return iLastElement;
         }
      }
   }

   /**
    * 获得旧字符串的单词查找树位置
    *
    * @param sText             文本
    * @param sArray            数组
    * @param iLessPointer      少指针
    * @param iMorePointer      多指针
    * @param iLastElement      最后一个元素
    * @param bDontAddNewString 不添加新字符串
    * @return int
    */
   public static int i_GetTriePositionForString_old(String sText, String[] sArray, int[] iLessPointer, int[] iMorePointer, int iLastElement, boolean bDontAddNewString) {
      int iTriePosition = 0;
      int iLastTriePosition = 0;
      if (iLastElement == 0) {
         iLastElement = 1;
         sArray[iLastElement] = sText;
         iLessPointer[iLastElement] = 0;
         iMorePointer[iLastElement] = 0;
         return 1;
      } else {
         iTriePosition = 1;

//         int iLastTriePosition;
         label33:
         do {
            do {
               iLastTriePosition = iTriePosition;
               if (sText.compareTo(sArray[iTriePosition]) < 0) {
                  iTriePosition = iLessPointer[iTriePosition];
                  continue label33;
               }

               if (sText.compareTo(sArray[iTriePosition]) <= 0) {
                  return iTriePosition;
               }

               iTriePosition = iMorePointer[iTriePosition];
            } while(iTriePosition != 0);

            if (bDontAddNewString) {
               return 0;
            }

            ++iLastElement;
            sArray[iLastElement] = sText;
            iLessPointer[iLastElement] = 0;
            iMorePointer[iLastElement] = 0;
            iMorePointer[iLastTriePosition] = iLastElement;
            return iLastElement;
         } while(iTriePosition != 0);

         if (bDontAddNewString) {
            return 0;
         } else {
            ++iLastElement;
            sArray[iLastElement] = sText;
            iLessPointer[iLastElement] = 0;
            iMorePointer[iLastElement] = 0;
            iLessPointer[iLastTriePosition] = iLastElement;
            return iLastElement;
         }
      }
   }

   /**
    * 获取字符串的单词查找树位置并添加计数
    *
    * @param sText             文本
    * @param sArray            数组
    * @param iCountArray       计算数组
    * @param iLessPointer      少指针
    * @param iMorePointer      多指针
    * @param iFirstElement     第一个元素
    * @param iLastElement      最后一个元素
    * @param bDontAddNewString 不添加新字符串
    * @param iCount            计数
    * @return int
    */
   public static int i_GetTriePositionForStringAndAddCount(String sText, String[] sArray, int[] iCountArray, int[] iLessPointer, int[] iMorePointer, int iFirstElement, int iLastElement, boolean bDontAddNewString, int iCount) {
      int iPos = i_GetTriePositionForString(sText, sArray, iLessPointer, iMorePointer, iFirstElement, iLastElement, bDontAddNewString);
      if (iPos >= 0) {
         int var10002 = iCountArray[iPos]++;
      }

      return iPos;
   }
}
