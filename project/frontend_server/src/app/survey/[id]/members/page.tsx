"use client";
import { MoreHorizontal, PlusCircle } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { SurveyUserSearchDialog } from "@/components/app/survey-user-search-dialog";
import { useCookies } from "next-client-cookies";
import { userInfo } from "@/actions/user";
import { surveyInfo } from "@/actions/survey";
import {
  surveyAddMember,
  surveyDeleteMember,
} from "@/controller/survey-members";

function UsernameTableCell({ userID }: { userID: number }) {
  const { data, isLoading, isError } = userInfo({
    userID,
  });
  if (isLoading) return <TableCell>Loading...</TableCell>;
  if (isError) return <TableCell>Error</TableCell>;
  return <TableCell className="font-medium">{data.data.username}</TableCell>;
}

export function MembersTable({
  surveyID,
  surveyInfo,
  members,
}: {
  surveyID: number;
  surveyInfo: any;
  members: any;
}) {
  const cookies = useCookies();
  return (
    <Card x-chunk="dashboard-06-chunk-0">
      <CardHeader>
        <CardTitle>Members</CardTitle>
        <CardDescription>Manage members.</CardDescription>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Username</TableHead>
              <TableHead>Role</TableHead>
              <TableHead>
                <span className="sr-only">Actions</span>
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {members.owners.map((memberID: number) => (
              <TableRow key={memberID}>
                <UsernameTableCell userID={memberID} />
                <TableCell>
                  <Badge variant="outline">Owner</Badge>
                </TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button aria-haspopup="true" size="icon" variant="ghost">
                        <MoreHorizontal className="h-4 w-4" />
                        <span className="sr-only">Toggle menu</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem>Edit</DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
            {members.members.map((memberID: number) => (
              <TableRow key={memberID}>
                <UsernameTableCell userID={memberID} />
                <TableCell>
                  <Badge variant="outline">Member</Badge>
                </TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button aria-haspopup="true" size="icon" variant="ghost">
                        <MoreHorizontal className="h-4 w-4" />
                        <span className="sr-only">Toggle menu</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem>
                        <Button
                          variant="destructive"
                          size="sm"
                          onClick={() =>
                            surveyDeleteMember({
                              token: cookies.get("token") as string,
                              surveyID,
                              surveyInfo,
                              userID: memberID,
                            })
                          }
                        >
                          Delete
                        </Button>
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
      <CardFooter>
        <div className="text-xs text-muted-foreground">
          {members.owners.length} owners, {members.members.length} members
        </div>
      </CardFooter>
    </Card>
  );
}

export default function Members({ params }: { params: { id: number } }) {
  const cookies = useCookies();
  const { data, isLoading, isError } = surveyInfo({ surveyID: params.id });
  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error</div>;
  const allMembers = {
    owners: data.data.owners,
    members: data.data.members,
  };
  return (
    <div className="flex flex-col sm:gap-4 sm:py-4 sm:pl-14">
      <main className="grid flex-1 items-start gap-4 p-4 sm:px-6 sm:py-0 md:gap-8">
        <Tabs defaultValue="all">
          <div className="flex items-center">
            <TabsList>
              <TabsTrigger value="all">All</TabsTrigger>
              <TabsTrigger value="grouped">Grouped</TabsTrigger>
              <TabsTrigger value="ungrouped">Ungrouped</TabsTrigger>
            </TabsList>
            <div className="ml-auto flex items-center gap-2">
              <SurveyUserSearchDialog
                callback={({ userID }: { userID: number }) => {
                  surveyAddMember({
                    token: cookies.get("token") as string,
                    surveyID: params.id,
                    surveyInfo: data.data.info,
                    userID: userID,
                  });
                }}
              >
                <Button size="sm" className="h-8 gap-1">
                  <PlusCircle className="h-3.5 w-3.5" />
                  <span className="sr-only sm:not-sr-only sm:whitespace-nowrap">
                    Add Members
                  </span>
                </Button>
              </SurveyUserSearchDialog>
            </div>
          </div>
          <TabsContent value="all">
            <MembersTable
              surveyID={params.id}
              surveyInfo={data.data.info}
              members={allMembers}
            />
          </TabsContent>
          <TabsContent value="grouped">
            <MembersTable
              surveyID={params.id}
              surveyInfo={data.data.info}
              members={allMembers}
            />
          </TabsContent>
          <TabsContent value="ungrouped">
            <MembersTable
              surveyID={params.id}
              surveyInfo={data.data.info}
              members={allMembers}
            />
          </TabsContent>
        </Tabs>
      </main>
    </div>
  );
}
